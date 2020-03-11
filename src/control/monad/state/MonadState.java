package control.monad.state;

import control.copy.Copiable;
import data.product.Product;

import java.util.function.Function;

/**
 * A state computation is a computation parameterized by an environment (implicitly propagated).
 * @param <Env> The environment type
 * @param <A> The resulting type
 */
public interface MonadState<Env extends Copiable<Env>, A> extends Function<Env, Product<Env, A>> {
    @Override
    default Product<Env, A> apply(Env env) {
        return read(env);
    }

    /**
     * Executes a state computation given an initial environment.
     * @param e
     * @return
     */
    Product<Env, A> read(Env e);

    // instance Monad (Reader env)

    /**
     * Binds two state computations, evaluating them one after the other.
     * @param f
     * @param <B>
     * @return
     */
    default <B> MonadState<Env, B> bind(Function<A, MonadState<Env, B>> f) {
        return env -> {
            Product<Env, A> res = this.read(env);
            return f.apply(res.snd).read(res.fst);
        };
    }

    // instance Functor (Reader env)

    /**
     * Lifts a function to a state computation.
     * @param f
     * @param <B>
     * @return
     */
    default <B> MonadState<Env, B> fmap(Function<A, B> f) {
        return env -> {
            Product<Env, A> res = this.read(env);
            return new Product<>(res.fst, f.apply(res.snd));
        };
    }

    // instance Applicative (Reader env)

    /**
     * Lifts a pure value in a state computation.
     * @param val
     * @param <Env>
     * @param <A>
     * @return
     */
    static <Env extends Copiable<Env>, A> MonadState<Env, A> pure(A val) {
        return env -> new Product<>(env, val);
    }

    // ...

    /**
     * Puts a new environment in place of the other.
     * @param e
     * @return
     */
    default MonadState<Env, Void> put(Env e) {
        return env -> new Product<>(e, null);
    }

    /**
     * Gets the current environment.
     * @return
     */
    default MonadState<Env, Env> get() {
        return env -> new Product<>(env, env);
    }

    /**
     * Modifies the current environment, applying a function to it.
     * @param f
     * @return
     */
    default MonadState<Env, Void> modify(Function<Env, Env> f) {
        return env -> new Product<>(f.apply(env), null);
    }

    /**
     * Executes a state computation, modifying the environment before and restoring the old environment after.
     * @param f
     * @return
     */
    default MonadState<Env, A> local(Function<Env, Env> f) {
        return env -> get()
            .bind(env_ -> modify(f)
                .bind(void_ -> this)
                .bind(res -> put(env)
                    .bind(void_ -> MonadState.pure(res))))
            .read(env);
    }
}

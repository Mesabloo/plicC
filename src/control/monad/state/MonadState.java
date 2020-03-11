package control.monad.state;

import control.copy.Copiable;
import data.product.Product;

import java.util.function.Function;

public interface MonadState<Env extends Copiable<Env>, A> extends Function<Env, Product<Env, A>> {
    @Override
    default Product<Env, A> apply(Env env) {
        return read(env);
    }

    Product<Env, A> read(Env e);

    // instance Monad (Reader env)
    default <B> MonadState<Env, B> bind(Function<A, MonadState<Env, B>> f) {
        return env -> {
            Product<Env, A> res = this.read(env);
            return f.apply(res.snd).read(res.fst);
        };
    }

    // instance Functor (Reader env)
    default <B> MonadState<Env, B> fmap(Function<A, B> f) {
        return env -> {
            Product<Env, A> res = this.read(env);
            return new Product<>(res.fst, f.apply(res.snd));
        };
    }

    // instance Applicative (Reader env)
    static <Env extends Copiable<Env>, A> MonadState<Env, A> pure(A val) {
        return env -> new Product<>(env, val);
    }

    // ...
    default MonadState<Env, Void> put(Env e) {
        return env -> new Product<>(e, null);
    }

    default MonadState<Env, Env> get() {
        return env -> new Product<>(env, env);
    }

    default MonadState<Env, Void> modify(Function<Env, Env> f) {
        return env -> new Product<>(f.apply(env), null);
    }

    default MonadState<Env, A> local(Function<Env, Env> f) {
        return env -> get()
            .bind(env_ -> modify(f)
                .bind(void_ -> this)
                .bind(res -> put(env)
                    .bind(void_ -> MonadState.pure(res))))
            .read(env);
    }
}

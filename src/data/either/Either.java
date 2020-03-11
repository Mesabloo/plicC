package data.either;

import java.util.function.Function;

/**
 * A data type to represent a dual possibility: either a resource of type A or a resource of type B
 *
 * @param <A> The first (left) resource type
 * @param <B> The second (right) resource type
 */
public abstract class Either<A, B> {
    /**
     * The left possibility of the {@link Either} data type.
     * @param <A>
     * @param <B> - Phantom type
     */
    public static class Left<A, B> extends Either<A, B> {
        private A left;

        public Left(A a) {
            this.left = a;
        }

        @Override
        public boolean isLeft() {
            return true;
        }

        @Override
        public <C> Either<A, C> fmap(Function<B, C> fun) {
            return new Left<>(this.left);
        }

        @Override
        public B fromRight() {
            throw new IllegalStateException("Cannot call `fromRight()` on Left class");
        }

        @Override
        public A fromLeft() {
            return this.left;
        }

        @Override
        public String toString() {
            return "Left(" + this.left.toString() + ")";
        }
    }

    public static class Right<A, B> extends Either<A, B> {
        public B right;

        public Right(B b) {
            this.right = b;
        }

        @Override
        public boolean isLeft() {
            return false;
        }

        @Override
        public <C> Either<A, C> fmap(Function<B, C> fun) {
            return new Right<>(fun.apply(this.right));
        }

        @Override
        public B fromRight() {
            return this.right;
        }

        @Override
        public A fromLeft() {
            throw new IllegalStateException("Cannot call `fromLeft()` on Right class");
        }

        @Override
        public String toString() {
            return "Right(" + this.right.toString() + ")";
        }
    }

    /**
     * @return whether the current {@link Either} instance is a left part or not.
     */
    public abstract boolean isLeft();

    /**
     * Lifts a function to an {@link Either} computation.
     * @param <C>
     * @param fun The function to lift
     * @return A new {@link Either} computation
     */
    public abstract <C> Either<A, C> fmap(Function<B, C> fun);

    /**
     * Retrieves the {@link Right} part of an {@link Either}.
     * @throws IllegalStateException if it is a {@link Left} part.
     * @return
     */
    public abstract B fromRight();

    /**
     * Retrieves the {@link Left} part of an {@link Either}.
     * @throws IllegalStateException if it is a {@link Right} part.
     * @return
     */
    public abstract A fromLeft();

    @Override
    public abstract String toString();

    /**
     * Lifts a value to the {@link Right} part of an {@link Either}.
     * @param b
     * @param <A>
     * @param <B>
     * @return
     */
    public static <A, B> Right<A, B> right(B b) {
        return new Right<>(b);
    }

    /**
     * Lifts a value to the {@link Left} part of an {@link Either}.
     * @param a
     * @param <A>
     * @param <B>
     * @return
     */
    public static <A, B> Left<A, B> left(A a) {
        return new Left<>(a);
    }
}

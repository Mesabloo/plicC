package data.either;

import java.util.function.Function;

public abstract class Either<A, B> {
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

    public abstract boolean isLeft();
    public abstract <C> Either<A, C> fmap(Function<B, C> fun);
    public abstract B fromRight();
    public abstract A fromLeft();

    @Override
    public abstract String toString();

    public static <A, B> Right<A, B> right(B b) {
        return new Right<>(b);
    }

    public static <A, B> Left<A, B> left(A a) {
        return new Left<>(a);
    }
}

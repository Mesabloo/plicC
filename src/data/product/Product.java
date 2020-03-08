package data.product;

import java.util.function.Function;

public class Product<A, B> {
    public A fst;
    public B snd;

    public Product(A a, B b) {
        this.fst = a;
        this.snd = b;
    }

    // instance Functor (Product a)
    public <C> Product<A, C> fmap(Function<B, C> fun) {
        return new Product<>(this.fst, fun.apply(this.snd));
    }
}

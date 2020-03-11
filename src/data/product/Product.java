package data.product;

import java.util.function.Function;

/**
 * The product type (also known as a tuple, or pair (pairs are more known as a product type where <code>A ~ B</code>)).
 * @param <A>
 * @param <B>
 */
public class Product<A, B> {
    public A fst;
    public B snd;

    public Product(A a, B b) {
        this.fst = a;
        this.snd = b;
    }

    // instance Functor (Product a)

    /**
     * Lifts a function to a {@link Product} computation.
     * @param fun The function
     * @param <C>
     * @return A new product with its second argument mapped.
     */
    public <C> Product<A, C> fmap(Function<B, C> fun) {
        return new Product<>(this.fst, fun.apply(this.snd));
    }
}

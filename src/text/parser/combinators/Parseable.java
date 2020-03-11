package text.parser.combinators;

import control.copy.Copiable;
import data.either.Either;
import data.product.Product;
import text.parser.combinators.error.ParseError;

import java.util.ArrayList;
import java.util.function.Function;
import java.util.function.Predicate;

import static data.either.Either.left;
import static data.either.Either.right;

/**
 * The main interface to a parseable object.
 * It is a {@link Function} under the hood.
 *
 * This interface is lazy. A parser is evaluated only when you tell it to {@link Parseable#parse(Stream_)}.
 * It can thus be composed using combinators without need to evaluate each time.
 * @param <Token> The token type of the {@link Stream_}
 * @param <Stream_> The stream manipulated by the parser
 * @param <A> The return type
 */
public interface Parseable<Token, Stream_ extends Stream<Token> & Copiable<Stream_>, A> extends Function<Stream_, Product<Stream_, Either<ParseError<Token, Stream_>, A>>> {
    /**
     * @see Function#apply(Object)
     */
    default Product<Stream_, Either<ParseError<Token, Stream_>, A>> parse(Stream_ r) {
        return this.apply(r);
    }

    /**
     * A parser told to satisfy a given {@link Predicate} on the first element of the stream.
     * Fails with a {@link ParseError} if the {@link Predicate} does not hold.
     * @param pred The predicate
     * @param <Token>
     * @param <Stream_>
     * @return Either a result or a {@link ParseError}
     */
    static <Token, Stream_ extends Stream<Token> & Copiable<Stream_>> Parseable<Token, Stream_, Token> satisfy(Predicate<Token> pred) {
        return stream -> {
            if (stream.eof())
                return new Product<>(stream, left(ParseError.eof(stream)));

            Token c = stream.lookahead();
            if (pred.test(c)) {
                stream.next();
                return new Product<>(stream, right(c));
            }
            return new Product<>(stream, left(ParseError.tokens(stream, new ArrayList<Token>() {{ this.add(c); }})));
        };
    }

    // instance Functor Parseable

    /**
     * Lifts a function to a {@link Parseable} computation.
     * @param fun The function
     * @param <B>
     * @return
     */
    default <B> Parseable<Token, Stream_, B> fmap(Function<A, B> fun) {
        return stream -> this.parse(stream).fmap(e -> e.fmap(fun));
    }

    // instance Applicative Parseable

    /**
     * Lifts a pure value to a {@link Parseable} computation.
     * @param val The value
     * @param <Token>
     * @param <Stream_>
     * @param <A>
     * @return The value lifted to a {@link Parseable}
     */
    static <Token, Stream_ extends Stream<Token> & Copiable<Stream_>, A> Parseable<Token, Stream_, A> pure(A val) {
        return stream -> new Product<>(stream, right(val));
    }

    /**
     * Applies many times (possibly none) the {@link Parseable} to the {@link Stream_}.
     * This combinator never fails.
     * @return An {@link ArrayList} containing all the results (possibly none)
     */
    default Parseable<Token, Stream_, ArrayList<A>> many() {
        return this.bind(r1 -> this.many().bind(rs -> {
            ArrayList<A> acc = new ArrayList<>();
            acc.add(r1);
            acc.addAll(rs);
            return pure(acc);
        })).orElse(pure(new ArrayList<>()));
    }

    /**
     * Same as {@link Parseable#many()} but fails if the parser fails on the first time.
     */
    default Parseable<Token, Stream_, ArrayList<A>> some() {
        return this.many().ap(this.fmap(p_ -> ps -> new ArrayList<A>() {{ this.add(p_); this.addAll(ps); }}));
    }

    /**
     * Applies a lifted function to a {@link Parseable}.
     * @param fun
     * @param <B>
     * @return
     */
    default <B> Parseable<Token, Stream_, B> ap(Parseable<Token, Stream_, Function<A, B>> fun) {
        return fun.bind(f -> this.bind(x -> pure(f.apply(x))));
    }

    // instance Alternative Parseable

    /**
     * An always-failing parser.
     * @param <Token>
     * @param <Stream_>
     * @param <A>
     * @return A {@link ParseError}
     */
    static <Token, Stream_ extends Stream<Token> & Copiable<Stream_>, A> Parseable<Token, Stream_, A> empty() {
        return stream -> new Product<>(stream, left(ParseError.empty(stream)));
    }

    /**
     * Combines two {@link Parseable} by running the second if the first fails.
     * @param p
     * @return
     */
    default Parseable<Token, Stream_, A> orElse(Parseable<Token, Stream_, A> p) {
        return stream -> {
            Product<Stream_, Either<ParseError<Token, Stream_>, A>> res = this.apply(stream);
            if (res.snd.isLeft()) {
                return p.apply(res.fst);
            }
            return res;
        };
    }

    // instance Monad Parseable

    /**
     * Binds a {@link Parseable} to a {@link Function} returning another {@link Parseable}.
     * @param fun
     * @param <B>
     * @return
     */
    default <B> Parseable<Token, Stream_, B> bind(Function<A, Parseable<Token, Stream_, B>> fun) {
        return stream -> {
            Product<Stream_, Either<ParseError<Token, Stream_>, A>> res1 = this.parse(stream);
            if (res1.snd.isLeft())
                return new Product<>(res1.fst, left(res1.snd.fromLeft()));
            return fun.apply(res1.snd.fromRight()).parse(res1.fst);
        };
    }

    /**
     * Sequences two {@link Parseable}s and keeps the result of the second.
     * @param p
     * @param <B>
     * @return
     */
    default <B> Parseable<Token, Stream_, B> then(Parseable<Token, Stream_, B> p) {
        return this.bind(ignore -> p);
    }

    /**
     * Same as {@link Parseable#then(Parseable)} but keeps the result of the first {@link Parseable}.
     * @param p
     * @param <B>
     * @return
     */
    default <B> Parseable<Token, Stream_, A> then_(Parseable<Token, Stream_, B> p) {
        return this.bind(res -> p.then(pure(res)));
    }

    // .......

    /**
     * Take multiple tokens while a {@link Predicate} holds.
     * @param pred
     * @param <Token>
     * @param <Stream_>
     * @return
     */
    static <Token, Stream_ extends Stream<Token> & Copiable<Stream_>> Parseable<Token, Stream_, ArrayList<Token>> takeWhile(Predicate<Token> pred) {
        return Parseable.<Token, Stream_>satisfy(pred).many();
    }

    /**
     * Tries to apply a {@link Parseable}. If it fails, fallback to the beginning {@link Stream_}.
     * @return
     */
    default Parseable<Token, Stream_, A> try_() {
        return stream -> {
            Stream_ tmp = stream.copy();
            Product<Stream_, Either<ParseError<Token, Stream_>, A>> res = this.parse(stream);
            if (res.snd.isLeft())
                return new Product<>(tmp, left(res.snd.fromLeft()));
            return res;
        };
    }

    /**
     * Tries to apply a parser on the stream. If the parser succeeds, the stream is not updated.
     * @param p
     * @param <Token>
     * @param <Stream_>
     * @param <A>
     * @return
     */
    static <Token, Stream_ extends Stream<Token> & Copiable<Stream_>, A> Parseable<Token, Stream_, A> lookahead(Parseable<Token, Stream_, A> p) {
        return stream -> {
            Stream_ oldStream = stream.copy();
            Product<Stream_, Either<ParseError<Token, Stream_>, A>> res = p.parse(stream);
            if (!res.snd.isLeft())
                return new Product<>(oldStream, right(res.snd.fromRight()));
            return res;
        };
    }

    /**
     * Voids a {@link Parseable} (that is, making it return {@link Void}).
     * @return Nothing
     */
    default Parseable<Token, Stream_, Void> void_() {
        return stream -> this.fmap(x -> (Void) null).parse(stream);
    }

    /**
     * Fails if not at the end of the stream.
     * @param <Token>
     * @param <Stream_>
     * @return
     */
    static <Token, Stream_ extends Stream<Token> & Copiable<Stream_>> Parseable<Token, Stream_, Void> eof() {
        return stream -> new Product<>(stream, stream.eof() ? right(null) : left(ParseError.eof(stream)));
    }

    /**
     * Helper to get the current line number.
     * @param <Token>
     * @param <Stream_>
     * @return
     */
    static <Token, Stream_ extends Stream<Token> & Copiable<Stream_>> Parseable<Token, Stream_, Long> lineNumber() {
        return stream -> new Product<>(stream, right(stream.getLineNumber()));
    }
}

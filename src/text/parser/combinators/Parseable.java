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

public interface Parseable<Token, Stream_ extends Stream<Token> & Copiable<Stream_>, A> extends Function<Stream_, Product<Stream_, Either<ParseError<Token, Stream_>, A>>> {
    String label = ""; // Handle later

    default Product<Stream_, Either<ParseError<Token, Stream_>, A>> parse(Stream_ r) {
        return this.apply(r);
    }

    static <Token, Stream_ extends Stream<Token> & Copiable<Stream_>> Parseable<Token, Stream_, Token> satisfy(Predicate<Token> pred) {
        return stream -> {
            if (stream.eof())
                return new Product<>(stream, left(ParseError.eof(stream)));

            Token c = stream.lookahead();
            if (pred.test(c)) {
                stream.next();
                return new Product<>(stream, right(c));
            }
            return new Product<>(stream, left(label.isEmpty() ?
                ParseError.tokens(stream, new ArrayList<Token>() {{ this.add(c); }}) :
                ParseError.label(stream, label)));
        };
    }

    // instance Functor Parseable
    default <B> Parseable<Token, Stream_, B> fmap(Function<A, B> fun) {
        return stream -> this.parse(stream).fmap(e -> e.fmap(fun));
    }

    // instance Applicative Parseable
    static <Token, Stream_ extends Stream<Token> & Copiable<Stream_>, A> Parseable<Token, Stream_, A> pure(A val) {
        return stream -> new Product<>(stream, right(val));
    }

    default Parseable<Token, Stream_, ArrayList<A>> many() {
        return this.bind(r1 -> this.many().bind(rs -> {
            ArrayList<A> acc = new ArrayList<>();
            acc.add(r1);
            acc.addAll(rs);
            return pure(acc);
        })).orElse(pure(new ArrayList<>()));
    }

    default Parseable<Token, Stream_, ArrayList<A>> some() {
        return this.many().ap(this.fmap(p_ -> ps -> new ArrayList<A>() {{ this.add(p_); this.addAll(ps); }}));
    }

    default <B> Parseable<Token, Stream_, B> ap(Parseable<Token, Stream_, Function<A, B>> fun) {
        return fun.bind(f -> this.bind(x -> pure(f.apply(x))));
    }

    // instance Alternative Parseable
    static <Token, Stream_ extends Stream<Token> & Copiable<Stream_>, A> Parseable<Token, Stream_, A> empty() {
        return stream -> new Product<>(stream, left(ParseError.empty(stream)));
    }

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
    default <B> Parseable<Token, Stream_, B> bind(Function<A, Parseable<Token, Stream_, B>> fun) {
        return stream -> {
            Product<Stream_, Either<ParseError<Token, Stream_>, A>> res1 = this.parse(stream);
            if (res1.snd.isLeft())
                return new Product<>(res1.fst, left(res1.snd.fromLeft()));
            return fun.apply(res1.snd.fromRight()).parse(res1.fst);
        };
    }

    default <B> Parseable<Token, Stream_, B> then(Parseable<Token, Stream_, B> p) {
        return this.bind(ignore -> p);
    }

    default <B> Parseable<Token, Stream_, A> then_(Parseable<Token, Stream_, B> p) {
        return this.bind(res -> p.then(pure(res)));
    }

    // .......
    static <Token, Stream_ extends Stream<Token> & Copiable<Stream_>> Parseable<Token, Stream_, ArrayList<Token>> takeWhile(Predicate<Token> pred) {
        return Parseable.<Token, Stream_>satisfy(pred).many();
    }

    default Parseable<Token, Stream_, A> try_() {
        return stream -> {
            Stream_ tmp = stream.copy();
            Product<Stream_, Either<ParseError<Token, Stream_>, A>> res = this.parse(stream);
            if (res.snd.isLeft())
                return new Product<>(tmp, left(res.snd.fromLeft()));
            return res;
        };
    }

    default Parseable<Token, Stream_, Void> void_() {
        return stream -> this.fmap(x -> (Void) null).parse(stream);
    }

    static <Token, Stream_ extends Stream<Token> & Copiable<Stream_>> Parseable<Token, Stream_, Void> eof() {
        return stream -> new Product<>(stream, stream.eof() ? right(null) : left(ParseError.eof(stream)));
    }

    static <Token, Stream_ extends Stream<Token> & Copiable<Stream_>> Parseable<Token, Stream_, Long> lineNumber() {
        return stream -> new Product<>(stream, right(stream.getLineNumber()));
    }

//    default Parseable<Token, Stream_, A> labelled(String lbl) {
//        label = lbl;
//        return this;
//    }
}

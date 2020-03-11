package plic.lexer.combinators;

import data.either.Either;
import data.product.Product;
import text.parser.combinators.Parseable;
import plic.lexer.stream.Reader;
import text.parser.combinators.error.ParseError;

import static data.either.Either.left;
import static data.either.Either.right;

public final class String_ {
    /**
     * Matches a given string.
     * @param s
     * @return
     */
    public static Parseable<Character, Reader, String> string(String s) {
        return stream -> {
            StringBuilder bd = new StringBuilder();
            Reader r = stream;

            for (char c : s.toCharArray()) {
                Product<Reader, Either<ParseError<Character, Reader>, Character>> res = character(c).parse(r);
                if (res.snd.isLeft())
                    return new Product<>(res.fst, left(res.snd.fromLeft()));

                r = res.fst;
                bd.append(res.snd.fromRight());
            }

            return new Product<>(r, right(bd.toString()));
        };
    }

    /**
     * Matches a given character.
     * @param c
     * @return
     */
    public static Parseable<Character, Reader, Character> character(Character c) {
        return s -> Parseable.<Character, Reader>satisfy(c1 -> c1 == c).apply(s);
    }

    /**
     * Matches any character.
     * @return
     */
    public static Parseable<Character, Reader, Character> anyChar() {
        return s -> Parseable.<Character, Reader>satisfy(c -> true).apply(s);
    }
}

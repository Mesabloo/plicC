package plic.lexer.tokenizers;

import data.either.Either;
import data.product.Product;
import plic.lexer.Lexec;
import text.parser.combinators.Parseable;
import plic.lexer.stream.Reader;
import plic.lexer.token.IntegerToken;
import plic.lexer.token.Token;
import text.parser.combinators.error.ParseError;

import java.util.ArrayList;
import java.util.function.Predicate;

/**
 * Matches an integer according to the grammar.
 */
public class IntegerLexer implements Lexec<Token> {
    @Override
    public Product<Reader, Either<ParseError<Character, Reader>, Token>> apply(Reader reader) {
        return Parseable.<Character, Reader>satisfy(isDigit()).some()
            .fmap(this::toNumber)
            .bind(nb -> Parseable.<Character, Reader>lineNumber()
                .fmap(line -> new IntegerToken(line, nb)))
            .fmap(t -> (Token) t)
            .parse(reader);
    }

    private Predicate<Character> isDigit() {
        return Character::isDigit;
    }

    private Long toNumber(ArrayList<Character> cs) {
        long acc = 0;
        for (Character c : cs) {
            acc += charToNumber(c);
            acc *= 10;
        }
        return acc / 10;
    }

    private long charToNumber(Character c) {
        switch (c) {
            case '0': return 0;
            case '1': return 1;
            case '2': return 2;
            case '3': return 3;
            case '4': return 4;
            case '5': return 5;
            case '6': return 6;
            case '7': return 7;
            case '8': return 8;
            case '9': return 9;
            default:
                throw new NumberFormatException(c + " is not a number");
        }
    }
}

package plic.lexer.tokenizers;

import data.either.Either;
import data.product.Product;
import plic.lexer.Lexec;
import text.parser.combinators.Parseable;
import plic.lexer.stream.Reader;
import plic.lexer.token.IdentifierToken;
import plic.lexer.token.Token;
import text.parser.combinators.error.ParseError;

import java.util.ArrayList;
import java.util.function.Predicate;

/**
 * Matches an identifier from the given grammar.
 */
public class IdentifierLexer implements Lexec<Token> {
    @Override
    public Product<Reader, Either<ParseError<Character, Reader>, Token>> apply(Reader reader) {
        return Parseable.<Character, Reader>satisfy(isValidIDChar()).some()
            .fmap(this::_toString)
            .bind(id -> Parseable.<Character, Reader>lineNumber()
                .fmap(line -> new IdentifierToken(line, id)))
            .fmap(t -> (Token) t)
            .parse(reader);
    }

    private Predicate<Character> isValidIDChar() {
        return c -> (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    private String _toString(ArrayList<Character> list) {
        final StringBuilder bd = new StringBuilder();
        list.forEach(bd::append);
        return bd.toString();
    }
}

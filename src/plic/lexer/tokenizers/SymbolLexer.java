package plic.lexer.tokenizers;

import data.either.Either;
import data.product.Product;
import plic.lexer.Lexec;
import text.parser.combinators.Parseable;
import plic.lexer.combinators.String_;
import plic.lexer.stream.Reader;
import plic.lexer.token.SymbolToken;
import plic.lexer.token.Token;
import text.parser.combinators.error.ParseError;

import java.util.ArrayList;

/**
 * Matches a reserved symbol according to the grammar.
 */
public class SymbolLexer implements Lexec<Token> {
    @Override
    public Product<Reader, Either<ParseError<Character, Reader>, Token>> apply(Reader reader) {
        return (String_.string(symbols.get(0))
                .orElse(String_.string(symbols.get(1))
                .orElse(String_.string(symbols.get(2)))
                .orElse(String_.string(symbols.get(3)))
                .orElse(Parseable.empty()))
            ).bind(sym -> Parseable.<Character, Reader>lineNumber()
                .fmap(line -> new SymbolToken(line, sym)))
            .fmap(t -> (Token) t)
            .parse(reader);
    }

    private static final ArrayList<String> symbols = new ArrayList<>();
    static {
        symbols.add("{");
        symbols.add("}");
        symbols.add(":=");
        symbols.add(";");
    }
}

package plic.parser.parsecs;

import data.either.Either;
import data.product.Product;
import plic.lexer.token.SymbolToken;
import plic.lexer.token.Token;
import plic.parser.Parsec;
import plic.parser.stream.Reader;
import text.parser.combinators.Parseable;
import text.parser.combinators.error.ParseError;

/**
 * Parses a given symbol.
 */
public class SymbolParsec implements Parsec<Void> {
    private String toFind;

    public SymbolParsec(String toFind) {
        this.toFind = toFind;
    }

    @Override
    public Product<Reader, Either<ParseError<Token, Reader>, Void>> apply(Reader reader) {
        return Parseable.<Token, Reader>satisfy(tk -> tk.getType() == Token.TokenType.SYMBOL
                                                && ((SymbolToken) tk).getSymbol().equals(toFind))
            .void_()
            .parse(reader);
    }
}

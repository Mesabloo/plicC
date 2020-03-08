package plic.parser.parsecs;

import data.either.Either;
import data.product.Product;
import plic.lexer.token.KeywordToken;
import plic.lexer.token.Token;
import plic.parser.Parsec;
import plic.parser.stream.Reader;
import text.parser.combinators.Parseable;
import text.parser.combinators.error.ParseError;

public class KeywordParsec implements Parsec<Void> {
    private String toFind;

    public KeywordParsec(String toFind) {
        this.toFind = toFind;
    }

    @Override
    public Product<Reader, Either<ParseError<Token, Reader>, Void>> apply(Reader reader) {
        return Parseable.<Token, Reader>satisfy(tk -> tk.getType() == Token.TokenType.KEYWORD
                                                && ((KeywordToken) tk).getKeyword().equals(toFind))
                                                    // ^^ safe cast, checked above
            .void_()
            .parse(reader);
    }
}

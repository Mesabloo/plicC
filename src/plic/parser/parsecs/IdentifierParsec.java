package plic.parser.parsecs;

import data.either.Either;
import data.product.Product;
import plic.lexer.token.IdentifierToken;
import plic.lexer.token.Token;
import plic.parser.Parsec;
import plic.parser.ast.IdentifierNode;
import plic.parser.stream.Reader;
import text.parser.combinators.Parseable;
import text.parser.combinators.error.ParseError;

public class IdentifierParsec implements Parsec<IdentifierNode> {
    public IdentifierParsec() {
    }

    @Override
    public Product<Reader, Either<ParseError<Token, Reader>, IdentifierNode>> apply(Reader reader) {
        return Parseable.<Token, Reader>satisfy(tk -> tk.getType() == Token.TokenType.IDENTIFIER)
            .fmap(tk -> (IdentifierToken) tk)
            .fmap(IdentifierNode::new)
            .parse(reader);
    }
}

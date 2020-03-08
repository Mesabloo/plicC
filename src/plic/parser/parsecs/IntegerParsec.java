package plic.parser.parsecs;

import data.either.Either;
import data.product.Product;
import plic.lexer.token.IntegerToken;
import plic.lexer.token.Token;
import plic.parser.Parsec;
import plic.parser.ast.IntegerNode;
import plic.parser.stream.Reader;
import text.parser.combinators.Parseable;
import text.parser.combinators.error.ParseError;

public class IntegerParsec implements Parsec<IntegerNode> {
    @Override
    public Product<Reader, Either<ParseError<Token, Reader>, IntegerNode>> apply(Reader reader) {
        return Parseable.<Token, Reader>satisfy(tk -> tk.getType() == Token.TokenType.INTEGER)
            .fmap(tk -> (IntegerToken) tk)
            .fmap(IntegerNode::new)
            .parse(reader);
    }
}

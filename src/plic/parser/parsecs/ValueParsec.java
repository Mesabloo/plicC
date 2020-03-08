package plic.parser.parsecs;

import data.either.Either;
import data.product.Product;
import plic.lexer.token.Token;
import plic.parser.Parsec;
import plic.parser.ast.ValueNode;
import plic.parser.stream.Reader;
import text.parser.combinators.error.ParseError;

public class ValueParsec implements Parsec<ValueNode> {
    @Override
    public Product<Reader, Either<ParseError<Token, Reader>, ValueNode>> apply(Reader reader) {
        return new IdentifierParsec().fmap(n -> (ValueNode) n)
            .orElse(new IntegerParsec().fmap(n -> (ValueNode) n)).parse(reader);
    }
}

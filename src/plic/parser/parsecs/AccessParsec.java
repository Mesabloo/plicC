package plic.parser.parsecs;

import data.either.Either;
import data.product.Product;
import plic.core.expression.AccessNode;
import plic.core.expression.ValueNode;
import plic.lexer.token.Token;
import plic.parser.Parsec;
import plic.parser.stream.Reader;
import text.parser.combinators.error.ParseError;

public class AccessParsec implements Parsec<ValueNode> {
    @Override
    public Product<Reader, Either<ParseError<Token, Reader>, ValueNode>> apply(Reader reader) {
        return new IdentifierParsec()
            .bind(id ->
                (new SymbolParsec("[")
                    .then(new ValueParsec())
                    .then_(new SymbolParsec("]")))
                .optional()
                .fmap(val -> val.isPresent()
                    ? new AccessNode(id, val.get())
                    : id
                )
            )
            .parse(reader);
    }
}

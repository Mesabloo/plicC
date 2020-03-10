package plic.parser.parsecs;

import data.either.Either;
import data.product.Product;
import plic.lexer.token.Token;
import plic.parser.Parsec;
import plic.parser.ast.DefinitionNode;
import plic.parser.stream.Reader;
import text.parser.combinators.error.ParseError;

public class DefinitionParsec implements Parsec<DefinitionNode> {
    @Override
    public Product<Reader, Either<ParseError<Token, Reader>, DefinitionNode>> apply(Reader reader) {
        return new IdentifierParsec()
            .bind(id -> new SymbolParsec(":=")
                .then(new ValueParsec())
                .fmap(v -> new DefinitionNode(id, v))
            ).then_(new SymbolParsec(";"))
            .parse(reader);
    }
}
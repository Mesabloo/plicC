package plic.parser.parsecs;

import data.either.Either;
import data.product.Product;
import plic.lexer.token.Token;
import plic.parser.Parsec;
import plic.parser.ast.DeclarationNode;
import plic.parser.stream.Reader;
import text.parser.combinators.Parseable;
import text.parser.combinators.error.ParseError;

public class DeclarationParsec implements Parsec<DeclarationNode> {
    @Override
    public Product<Reader, Either<ParseError<Token, Reader>, DeclarationNode>> apply(Reader reader) {
        return (new KeywordParsec("entier").bind(t -> Parseable.pure(DeclarationNode.Type.ENTIER)))
            .bind(ty -> new IdentifierParsec()
                .fmap(id -> new DeclarationNode(ty, id.getIdentifier()))
            )
            .then_(new SymbolParsec(";"))
            .parse(reader);
    }
}

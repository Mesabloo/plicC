package plic.parser.parsecs;

import data.either.Either;
import data.product.Product;
import plic.lexer.token.Token;
import plic.parser.Parsec;
import plic.core.instruction.DeclarationNode;
import plic.parser.stream.Reader;
import text.parser.combinators.error.ParseError;

/**
 * Parses a variable declaration according to the grammar.
 */
public class DeclarationParsec implements Parsec<DeclarationNode> {
    @Override
    public Product<Reader, Either<ParseError<Token, Reader>, DeclarationNode>> apply(Reader reader) {
        return (new KeywordParsec("entier")
                .<DeclarationNode.Type>fmap(t -> new DeclarationNode.TypeEntier())
            .orElse(new KeywordParsec("tableau")
                .then(new SymbolParsec("["))
                .then(new IntegerParsec())
                .then_(new SymbolParsec("]"))
                .fmap(t -> new DeclarationNode.TypeTableau((int) t.getInteger()))))
            .bind(ty -> new IdentifierParsec()
                .fmap(id -> new DeclarationNode(ty, id.getIdentifier()))
            )
            .then_(new SymbolParsec(";"))
            .parse(reader);
    }
}

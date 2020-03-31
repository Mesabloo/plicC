package plic.parser.parsecs;

import data.either.Either;
import data.product.Product;
import plic.core.expression.IdentifierNode;
import plic.core.instruction.InputInstrNode;
import plic.lexer.token.Token;
import plic.parser.Parsec;
import plic.parser.stream.Reader;
import text.parser.combinators.error.ParseError;

public class InputParsec implements Parsec<InputInstrNode> {
    @Override
    public Product<Reader, Either<ParseError<Token, Reader>, InputInstrNode>> apply(Reader reader) {
        return new KeywordParsec("lire")
            .then(new IdentifierParsec())
            .fmap(InputInstrNode::new)
            .then_(new SymbolParsec(";"))
            .parse(reader);
    }
}

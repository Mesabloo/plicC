package plic.parser.parsecs;

import data.either.Either;
import data.product.Product;
import plic.lexer.token.Token;
import plic.parser.Parsec;
import plic.core.OutputInstrNode;
import plic.parser.stream.Reader;
import text.parser.combinators.error.ParseError;

/**
 * Parses a {@code write} instruction.
 */
public class OutputParsec implements Parsec<OutputInstrNode> {
    @Override
    public Product<Reader, Either<ParseError<Token, Reader>, OutputInstrNode>> apply(Reader reader) {
        return new KeywordParsec("ecrire")
            .then(new ValueParsec())
            .fmap(OutputInstrNode::new)
            .then_(new SymbolParsec(";"))
            .apply(reader);
    }
}

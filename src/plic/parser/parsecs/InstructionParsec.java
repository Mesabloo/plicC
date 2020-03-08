package plic.parser.parsecs;

import data.either.Either;
import data.product.Product;
import plic.lexer.token.Token;
import plic.parser.Parsec;
import plic.parser.ast.InstructionNode;
import plic.parser.stream.Reader;
import text.parser.combinators.error.ParseError;

public class InstructionParsec implements Parsec<InstructionNode> {
    @Override
    public Product<Reader, Either<ParseError<Token, Reader>, InstructionNode>> apply(Reader reader) {
        return new DefinitionParsec()
            .fmap(def -> (InstructionNode) def)
            .parse(reader);
    }
}

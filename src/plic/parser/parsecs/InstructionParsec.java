package plic.parser.parsecs;

import data.either.Either;
import data.product.Product;
import plic.lexer.token.Token;
import plic.parser.Parsec;
import plic.core.instruction.InstructionNode;
import plic.parser.stream.Reader;
import text.parser.combinators.error.ParseError;

/**
 * Parses any instruction from the language.
 */
public class InstructionParsec implements Parsec<InstructionNode> {
    @Override
    public Product<Reader, Either<ParseError<Token, Reader>, InstructionNode>> apply(Reader reader) {
        return (new OutputParsec().fmap(def -> (InstructionNode) def))
            .orElse(new DefinitionParsec().fmap(def -> (InstructionNode) def))
            .orElse(new ConditionParsec().fmap(cond -> (InstructionNode) cond))
            .parse(reader);
    }
}

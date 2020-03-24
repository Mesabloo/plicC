package plic.parser.parsecs;

import data.either.Either;
import data.product.Product;
import plic.core.instruction.InstructionNode;
import plic.lexer.token.Token;
import plic.parser.Parsec;
import plic.core.BlockNode;
import plic.parser.stream.Reader;
import text.parser.combinators.error.ParseError;

import java.util.ArrayList;

/**
 * Parses a block of instructions according to the grammar.
 */
public class BlockParsec implements Parsec<BlockNode> {
    @Override
    public Product<Reader, Either<ParseError<Token, Reader>, BlockNode>> apply(Reader reader) {
        return new SymbolParsec("{")
            .then(new DeclarationParsec().try_().many())
            .bind(decls -> new InstructionParsec().try_().some()
                .fmap(instrs -> {
                    ArrayList<InstructionNode> list = new ArrayList<>(decls);
                    list.addAll(instrs);
                    return list;
                }))
            .fmap(BlockNode::new)
            .then_(new SymbolParsec("}"))
            .parse(reader);
    }
}

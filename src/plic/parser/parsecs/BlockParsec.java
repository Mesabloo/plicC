package plic.parser.parsecs;

import data.either.Either;
import data.product.Product;
import plic.lexer.token.Token;
import plic.parser.Parsec;
import plic.parser.ast.BlockNode;
import plic.parser.ast.TreeNode;
import plic.parser.stream.Reader;
import text.parser.combinators.error.ParseError;

import java.util.ArrayList;

public class BlockParsec implements Parsec<BlockNode> {
    @Override
    public Product<Reader, Either<ParseError<Token, Reader>, BlockNode>> apply(Reader reader) {
        return new SymbolParsec("{")
            .then(new DeclarationParsec().many())
            .bind(decls -> new InstructionParsec().try_().some()
                .fmap(instrs -> {
                    ArrayList<TreeNode> stts = new ArrayList<>();
                    stts.addAll(decls);
                    stts.addAll(instrs);
                    return stts;
                }))
            .fmap(BlockNode::new)
            .then_(new SymbolParsec("}"))
            .parse(reader);
    }
}

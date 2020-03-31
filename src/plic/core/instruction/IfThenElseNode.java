package plic.core.instruction;

import control.monad.state.MonadState;
import data.either.Either;
import data.product.Product;
import plic.core.BlockNode;
import plic.core.expression.ValueNode;
import plic.typechecker.core.SymbolTable;
import plic.typechecker.error.TypeError;

public class IfThenElseNode extends IfThenNode {
    private BlockNode else_;

    public IfThenElseNode(ValueNode cond, BlockNode t, BlockNode e) {
        super(cond, t);
        this.else_ = e;
    }

    @Override
    public Product<SymbolTable, Either<TypeError, SymbolTable>> typecheck(SymbolTable s) {
        return super
            .bind(ignore -> else_)
            .get()
            .fmap(s2 -> (Either<TypeError, SymbolTable>) Either.<TypeError, SymbolTable>right(s2))
            .read(s);
    }

    @Override
    public StringBuilder generateElseBranchIfNeeded(int indent) {
        long newBlock = blockCounter.get();
        return new StringBuilder()
            .append(genIndent(indent))
                .append("beqz $v0, _sinon")
                    .append(newBlock)
                    .append("\n");
    }

    @Override
    public StringBuilder generateElse(int indent) {
        long newBlock = blockCounter.get();
        return else_.generateMIPS(
            new StringBuilder()
                .append("# sinon\n")
                .append(genIndent(indent))
                    .append("_sinon")
                        .append(newBlock)
                        .append(":\n")
            , indent);
    }
}

package plic.core.instruction;

import control.monad.state.MonadState;
import data.either.Either;
import data.product.Product;
import plic.core.BlockNode;
import plic.core.expression.ValueNode;
import plic.typechecker.core.SymbolTable;
import plic.typechecker.core.Type;
import plic.typechecker.error.CannotUnifyTypes;
import plic.typechecker.error.TypeError;

public class IfThenNode extends ConditionalInstructionNode {
    protected ValueNode condition;
    protected BlockNode then;

    public IfThenNode(ValueNode cond, BlockNode t) {
        this.condition = cond;
        this.then = t;
    }

    @Override
    public Product<SymbolTable, Either<TypeError, SymbolTable>> typecheck(SymbolTable s) {
        return condition
            .fmap(ety -> ety
                .bind(ty -> ty == Type.BOOLEEN
                      ? Either.right(ty)
                      : Either.left(new CannotUnifyTypes(ty, Type.BOOLEEN))
                )
            )
            .bind(ignore -> then)
            .get()
            .fmap(s2 -> (Either<TypeError, SymbolTable>) Either.<TypeError, SymbolTable>right(s2))
            .read(s);
    }

    @Override
    public StringBuilder generateMIPS(StringBuilder builder, int indent) {
        long newBlock = blockCounter.preIncrement(1);
        return then.generateMIPS(
            condition.generateMIPSAsRHS(builder
                .append("# si ")
                    .append(condition.toString_(indent))
                    .append("\n")
                , indent)
                .append(generateElseBranchIfNeeded(indent))
                .append("# alors\n")
                .append(genIndent(indent))
                    .append("_alors")
                        .append(newBlock)
                        .append(":\n")
            , indent)
            .append(genIndent(indent + 4))
                .append("j _fsi")
                    .append(newBlock)
                    .append("\n")
            .append(generateElse(indent))
            .append(genIndent(indent))
                .append("_fsi")
                    .append(newBlock)
                    .append(":\n");
    }

    @Override
    public StringBuilder generateElseBranchIfNeeded(int indent) {
        return new StringBuilder();
    }

    @Override
    public StringBuilder generateElse(int indent) {
        return new StringBuilder();
    }
}

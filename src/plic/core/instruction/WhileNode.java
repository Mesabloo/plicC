package plic.core.instruction;

import data.either.Either;
import data.product.Product;
import plic.core.BlockNode;
import plic.core.expression.ValueNode;
import plic.typechecker.core.SymbolTable;
import plic.typechecker.core.Type;
import plic.typechecker.error.CannotUnifyTypes;
import plic.typechecker.error.TypeError;

public class WhileNode extends InstructionNode {
    private ValueNode condition;
    private BlockNode block;

    public WhileNode(ValueNode cond, BlockNode is) {
        this.condition = cond;
        this.block = is;
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
            .bind(ignore -> block)
            .get()
            .fmap(s2 -> (Either<TypeError, SymbolTable>) Either.<TypeError, SymbolTable>right(s2))
            .read(s);
    }

    @Override
    public StringBuilder generateMIPS(StringBuilder builder, int indent) {
        long newBlock = blockCounter.preIncrement(1);
        return block.generateMIPS(
            condition.generateMIPSAsRHS(
                builder
                    .append("# while ")
                        .append(condition.toString_(indent))
                        .append("\n")
                    .append(genIndent(indent))
                        .append("_while")
                            .append(newBlock)
                            .append(":\n")
                , indent + 4)
                .append(genIndent(indent + 4))
                    .append("beqz $v0, _endwhile")
                        .append(newBlock)
                        .append("\n")
            , indent)
            .append(genIndent(indent + 4))
                .append("j _while")
                    .append(newBlock)
                    .append("\n")
            .append(genIndent(indent))
                .append("_endwhile")
                    .append(newBlock)
                    .append(":\n");
    }
}

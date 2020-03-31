package plic.core.instruction;

import data.either.Either;
import data.product.Product;
import plic.core.BlockNode;
import plic.core.expression.IdentifierNode;
import plic.core.expression.ValueNode;
import plic.typechecker.core.SymbolTable;
import plic.typechecker.core.Type;
import plic.typechecker.error.CannotUnifyTypes;
import plic.typechecker.error.TypeError;

public class ForEachNode extends InstructionNode {
    private IdentifierNode index;
    private ValueNode rangeBegin;
    private ValueNode rangeEnd;
    private BlockNode block;

    public ForEachNode(IdentifierNode i, ValueNode rb, ValueNode re, BlockNode b) {
        this.index = i;
        this.rangeBegin = rb;
        this.rangeEnd = re;
        this.block = b;
    }

    @Override
    public Product<SymbolTable, Either<TypeError, SymbolTable>> typecheck(SymbolTable s) {
        return index
            .fmap(ety -> ety
                .bind(ty -> ty == Type.ENTIER
                        ? Either.right(Type.ENTIER)
                        : Either.left(new CannotUnifyTypes(ty, Type.ENTIER))
                )
            )
            .bind(ignore -> rangeBegin
                .fmap(ety -> ety
                    .bind(ty -> ty == Type.ENTIER
                            ? Either.right(Type.ENTIER)
                            : Either.left(new CannotUnifyTypes(ty, Type.ENTIER))
                    )
                )
            )
            .bind(ignore -> rangeEnd
                .fmap(ety -> ety
                    .bind(ty -> ty == Type.ENTIER
                            ? Either.right(Type.ENTIER)
                            : Either.left(new CannotUnifyTypes(ty, Type.ENTIER))
                    )
                )
            )
            .bind(ignore -> block)
            .get()
            .fmap(ignore -> Either.<TypeError, SymbolTable>right(s))
            .fmap(e -> (Either<TypeError, SymbolTable>) e)
            .read(s);
    }

    @Override
    public StringBuilder generateMIPS(StringBuilder builder, int indent) {
        long newBlock = blockCounter.preIncrement(1);
        return block.generateMIPS(
            rangeEnd.generateMIPSAsRHS(
                rangeBegin.generateMIPSAsRHS(
                    builder.append("# for ")
                            .append(index.getIdentifier())
                            .append(" in [")
                            .append(rangeBegin.toString_(0))
                            .append(" .. ")
                            .append(rangeEnd.toString_(0))
                            .append("]\n")
                    , indent)
                    .append(genIndent(indent))
                        .append("move $t2, $v0\n")
            , indent)
            .append(genIndent(indent))
                .append("move $t3, $v0\n")
            .append(genIndent(indent))
                .append("bgt $t2, $t3, _endforeach")
                    .append(newBlock)
                    .append("\n")
            .append(genIndent(indent))
                .append("sw $t2, __var_")
                    .append(index.getIdentifier())
                    .append("\n")
            .append(genIndent(indent))
                .append("_foreach")
                    .append(newBlock)
                    .append(":\n")
            , indent)
            .append(genIndent(indent + 4))
                .append("lw $t2, __var_")
                    .append(index.getIdentifier())
                    .append("\n")
            .append(genIndent(indent + 4))
                .append("addi $t2, $t2, 1\n")
            .append(genIndent(indent + 4))
                .append("sw $t2, __var_")
                    .append(index.getIdentifier())
                    .append("\n")
            .append(genIndent(indent + 4))
                .append("ble $t2, $t3, _foreach")
                    .append(newBlock)
                    .append("\n")
            .append(genIndent(indent))
                .append("_endforeach")
                    .append(newBlock)
                    .append(":\n");
    }
}

package plic.core.instruction;

import data.either.Either;
import data.product.Product;
import plic.core.expression.IdentifierNode;
import plic.typechecker.core.SymbolTable;
import plic.typechecker.core.Type;
import plic.typechecker.error.CannotUnifyTypes;
import plic.typechecker.error.TypeError;

public class InputInstrNode extends InstructionNode {
    private IdentifierNode readIn;

    public InputInstrNode(IdentifierNode n) {
        this.readIn = n;
    }

    @Override
    public Product<SymbolTable, Either<TypeError, SymbolTable>> typecheck(SymbolTable s) {
        return readIn
            .fmap(ety -> ety
                .bind(ty -> ty == Type.ENTIER
                      ? Either.right(ty)
                      : Either.left(new CannotUnifyTypes(ty, Type.ENTIER))
                )
            )
            .fmap(ignore -> Either.<TypeError, SymbolTable>right(s))
            .fmap(e -> (Either<TypeError, SymbolTable>) e)
            .read(s);
    }

    @Override
    public StringBuilder generateMIPS(StringBuilder builder, int indent) {
        return builder
            .append("# read ")
                .append(readIn.getIdentifier())
                .append("\n")
            .append(genIndent(indent))
                .append("la $a0, __var_")
                    .append(readIn.getIdentifier())
                    .append("\n")
            .append(genIndent(indent))
                .append("read ($a0)\n");
    }
}

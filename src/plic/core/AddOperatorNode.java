package plic.core;

import data.either.Either;
import data.product.Product;
import plic.typechecker.core.SymbolTable;
import plic.typechecker.core.Type;
import plic.typechecker.error.CannotUnifyTypes;
import plic.typechecker.error.TypeError;

import static data.either.Either.right;

public class AddOperatorNode extends BinaryOperatorNode {
    @Override
    public StringBuilder generateMIPSAsLHS(StringBuilder builder, int indent) {
        return builder
            .append("# Cannot generate addition as a LHS\n");
    }

    @Override
    public StringBuilder generateMIPSAsRHS(StringBuilder builder, int indent) {
        return e2.generateMIPSAsRHS(
            e1.generateMIPSAsRHS(builder
                    .append("# ")
                        .append(e1.toString_(0))
                        .append(" + ")
                        .append(e2.toString_(0))
                        .append("\n")
                , indent)
                .append(genIndent(indent))
                    .append("push ($v0, 4)\n")
            , indent)
            .append(genIndent(indent))
                .append("pop ($v1, 4)\n")
            .append(genIndent(indent))
                .append("add $v0, $v1, $v0\n");
    }

    @Override
    public StringBuilder generateMIPS(StringBuilder builder, int indent) {
        return generateMIPSAsRHS(builder, indent);
    }

    @Override
    public Product<SymbolTable, Either<TypeError, Type>> typecheck(SymbolTable s) {
        return e1
            .bind(ety1 -> e2
                .fmap(ety2 -> ety1
                    .bind(ty1 -> ety2
                        .bind(ty2 -> ty1 == Type.ENTIER
                            ? ty2 == Type.ENTIER
                                ? Either.right(Type.ENTIER)
                                : Either.left(new CannotUnifyTypes(ty2, Type.ENTIER))
                            : Either.left(new CannotUnifyTypes(ty1, Type.ENTIER))
                        )
                    )
                )
            )
            .read(s);
    }
}

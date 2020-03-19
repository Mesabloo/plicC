package plic.core;

import control.monad.state.MonadState;
import data.either.Either;
import data.product.Product;
import plic.typechecker.core.SymbolTable;
import plic.typechecker.core.Type;
import plic.typechecker.error.CannotUnifyTypes;
import plic.typechecker.error.TypeError;
import plic.typechecker.error.UndeclaredVariable;

import java.util.Optional;

public class DefinitionNode extends InstructionNode {
    private ValueNode lhs;
    private ValueNode rhs;

    public DefinitionNode(ValueNode l, ValueNode r) {
        this.lhs = l;
        this.rhs = r;
    }

    @Override
    public String toString_(int level) {
        String prefix = super.toString_(level);
        return prefix + lhs.toString_(level) + " := " + rhs.toString_(level);
    }

    public ValueNode lhs() {
        return this.lhs;
    }

    public ValueNode rhs() {
        return this.rhs;
    }

    @Override
    public Product<SymbolTable, Either<TypeError, SymbolTable>> typecheck(SymbolTable s) {
        return get()
            .bind(env -> this.lhs
                .bind(ty -> {
                    if (ty.isLeft())
                        return e_ -> new Product<SymbolTable, Either<TypeError, SymbolTable>>(e_, Either.left(ty.fromLeft()));
                    return this.rhs
                        .bind(ty2 -> {
                            if (ty2.isLeft())
                                return e_ -> new Product<>(e_, Either.left(ty2.fromLeft()));
                            Type t1 = ty.fromRight();
                            Type t2 = ty2.fromRight();
                            if (!t1.equals(t2))
                                return e_ -> new Product<>(e_, Either.left(new CannotUnifyTypes(t1, t2)));
                            return MonadState.pure(Either.right(env));
                        });
                })
            )
            .read(s);
    }

    @Override
    public StringBuilder generateMIPS(StringBuilder builder, int indent) {
        builder
            .append("# ")
                .append(lhs.toString_(0))
                .append(" := ")
                .append(rhs.toString_(0))
                .append("\n");
        lhs.generateMIPSAsLHS(builder, indent);
        rhs.generateMIPSAsRHS(builder, indent);

        return builder
            .append(genIndent(indent))
                .append("sw $v0, ($a0)\n");
    }
}

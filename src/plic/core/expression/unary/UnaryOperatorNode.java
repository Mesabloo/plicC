package plic.core.expression.unary;

import control.monad.state.MonadState;
import data.either.Either;
import plic.core.expression.ValueNode;
import plic.typechecker.core.SymbolTable;
import plic.typechecker.core.Type;
import plic.typechecker.error.CannotUnifyTypes;
import plic.typechecker.error.TypeError;

public abstract class UnaryOperatorNode extends ValueNode {
    protected ValueNode e1;

    public UnaryOperatorNode set(ValueNode v1) {
        this.e1 = v1;
        return this;
    }

    @Override
    public StringBuilder generateMIPSAsLHS(StringBuilder builder, int indent) {
        return builder.append("# Cannot generate unary operation as LHS\n");
    }

    @Override
    public StringBuilder generateMIPS(StringBuilder builder, int indent) {
        return generateMIPSAsRHS(builder, indent);
    }

    public MonadState<SymbolTable, Either<TypeError, Type>> check(String op) {
        switch (op) {
            case "-": // Int -> Int
                return e1.fmap(ety1 -> ety1
                    .bind(ty1 -> ty1 == Type.ENTIER
                        ? Either.right(Type.ENTIER)
                        : Either.left(new CannotUnifyTypes(ty1, Type.ENTIER))
                    )
                );
            case "!": // Bool -> Bool
                return e1.fmap(ety1 -> ety1
                    .bind(ty1 -> ty1 == Type.BOOLEEN
                        ? Either.right(Type.BOOLEEN)
                        : Either.left(new CannotUnifyTypes(ty1, Type.BOOLEEN))
                    )
                );
            default:
                return MonadState.pure(Either.left(new TypeError()));
        }
    }

    @Override
    public abstract String toString_(int level);
}

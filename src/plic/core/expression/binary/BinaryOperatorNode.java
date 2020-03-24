package plic.core.expression.binary;

import control.monad.state.MonadState;
import data.either.Either;
import plic.core.expression.ValueNode;
import plic.typechecker.core.SymbolTable;
import plic.typechecker.core.Type;
import plic.typechecker.error.CannotUnifyTypes;
import plic.typechecker.error.TypeError;

public abstract class BinaryOperatorNode extends ValueNode {
    protected ValueNode e1;
    protected ValueNode e2;

    public BinaryOperatorNode set(ValueNode v1, ValueNode v2) {
        this.e1 = v1;
        this.e2 = v2;
        return this;
    }

    @Override
    public StringBuilder generateMIPSAsLHS(StringBuilder builder, int indent) {
        return builder
            .append("# Cannot generate binary operation as a LHS\n");
    }

    @Override
    public StringBuilder generateMIPS(StringBuilder builder, int indent) {
        return generateMIPSAsRHS(builder, indent);
    }

    public StringBuilder generateRHSWith(StringBuilder builder, int indent, String instr) {
        return e2.generateMIPSAsRHS(
            e1.generateMIPSAsRHS(builder, indent)
                    .append(genIndent(indent))
                    .append("push ($v0, 4)\n")
            , indent)
            .append(genIndent(indent))
                .append("pop ($v1, 4)\n")
            .append(genIndent(indent))
                .append(instr)
                    .append(" $v0, $v1, $v0\n");
    }

    public MonadState<SymbolTable, Either<TypeError, Type>> check(String op) {
        switch (op) {
            case "+": // Int -> Int -> Int
            case "-": // Int -> Int -> Int
            case "*": // Int -> Int -> Int
                return e1.bind(ety1 -> e2
                    .fmap(ety2 -> ety1.bind(ty1 -> ety2
                        .bind(ty2 -> ty1 == Type.ENTIER
                            ? ty2 == Type.ENTIER
                                ? Either.right(Type.ENTIER)
                                : Either.left(new CannotUnifyTypes(ty2, Type.ENTIER))
                            : Either.left(new CannotUnifyTypes(ty1, Type.ENTIER))
                        )
                    ))
                );
            case ">": // Int -> Int -> Bool
            case "<": // Int -> Int -> Bool
            case ">=": // Int -> Int -> Bool
            case "<=": // Int -> Int -> Bool
            case "=": // Int -> Int -> Bool
            case "#": // Int -> Int -> Bool
                return e1.bind(ety1 -> e2
                    .fmap(ety2 -> ety1.bind(ty1 -> ety2
                        .bind(ty2 -> ty1 == Type.ENTIER
                            ? ty2 == Type.ENTIER
                                ? Either.right(Type.BOOLEEN)
                                : Either.left(new CannotUnifyTypes(ty2, Type.ENTIER))
                            : Either.left(new CannotUnifyTypes(ty1, Type.ENTIER))
                        )
                    ))
                );
            case "&&": // Bool -> Bool -> Bool
            case "||": // Bool -> Bool -> Bool
                return e1.bind(ety1 -> e2
                    .fmap(ety2 -> ety1.bind(ty1 -> ety2
                        .bind(ty2 -> ty1 == Type.BOOLEEN
                            ? ty2 == Type.BOOLEEN
                                ? Either.right(Type.BOOLEEN)
                                : Either.left(new CannotUnifyTypes(ty2, Type.BOOLEEN))
                            : Either.left(new CannotUnifyTypes(ty1, Type.BOOLEEN))
                        )
                    ))
                );
            default:
                return MonadState.pure(Either.left(new TypeError()));
        }
    }

    @Override
    public abstract String toString_(int level);
}

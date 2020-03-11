package plic.typechecker.checkers;

import control.monad.state.MonadState;
import data.either.Either;
import data.product.Product;
import plic.parser.ast.DeclarationNode;
import plic.parser.ast.DefinitionNode;
import plic.typechecker.TypeCheck;
import plic.typechecker.core.SymbolTable;
import plic.typechecker.error.CannotUnifyTypes;
import plic.typechecker.error.TypeError;

public class TCDefinition implements TypeCheck<SymbolTable> {
    private DefinitionNode node;

    public TCDefinition(DefinitionNode dn) {
        this.node = dn;
    }

    @Override
    public Product<SymbolTable, Either<TypeError, SymbolTable>> read(SymbolTable e) {
        return get()
            .bind(env -> new TCExpression(this.node.lhs())
                .bind(ty -> {
                    if (ty.isLeft())
                        return e_ -> new Product<SymbolTable, Either<TypeError, SymbolTable>>(e_, Either.left(ty.fromLeft()));
                    return new TCExpression(this.node.rhs())
                        .bind(ty2 -> {
                            if (ty2.isLeft())
                                return e_ -> new Product<>(e_, Either.left(ty2.fromLeft()));
                            DeclarationNode.Type t1 = ty.fromRight();
                            DeclarationNode.Type t2 = ty2.fromRight();
                            if (!t1.equals(t2))
                                return e_ -> new Product<>(e_, Either.left(new CannotUnifyTypes(t1, t2)));
                            return MonadState.pure(Either.right(env));
                        });
                    }
                )
            )
            .read(e);
    }
}

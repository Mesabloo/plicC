package plic.typechecker.checkers;

import control.monad.state.MonadState;
import data.either.Either;
import data.product.Product;
import plic.parser.ast.DeclarationNode;
import plic.typechecker.TypeCheck;
import plic.typechecker.core.SymbolTable;
import plic.typechecker.error.RedefinedVariable;
import plic.typechecker.error.TypeError;

import java.util.Optional;

public class TCDeclaration implements TypeCheck<SymbolTable> {
    private DeclarationNode node;

    public TCDeclaration(DeclarationNode dn) {
        this.node = dn;
    }

    @Override
    public Product<SymbolTable, Either<TypeError, SymbolTable>> read(SymbolTable e) {
        return get()
            .bind(env -> {
                Optional<DeclarationNode.Type> ty = env.getTypeOf(this.node.getName());
                if (ty.isPresent())
                    return e_ -> new Product<SymbolTable, Either<TypeError, SymbolTable>>(e, Either.left(new RedefinedVariable(this.node.getName())));
                env.add(this.node.getName(), this.node.getType());
                return MonadState.pure(Either.right(env));
            })
            .read(e);
    }
}

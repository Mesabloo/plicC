package plic.typechecker.checkers;

import data.either.Either;
import data.product.Product;
import plic.core.DeclarationNode;
import plic.core.IdentifierNode;
import plic.typechecker.TypeCheck;
import plic.typechecker.core.SymbolTable;
import plic.typechecker.error.TypeError;
import plic.typechecker.error.UndeclaredVariable;

import java.util.Optional;

public class TCIdentifier implements TypeCheck<DeclarationNode.Type> {
    private IdentifierNode node;

    public TCIdentifier(IdentifierNode in) {
        this.node = in;
    }

    @Override
    public Product<SymbolTable, Either<TypeError, DeclarationNode.Type>> read(SymbolTable e) {
        return get()
            .bind(env -> {
                Optional<DeclarationNode.Type> ty = env.getTypeOf(this.node.getIdentifier());
                if (!ty.isPresent())
                    return e_ -> new Product<>(e_, Either.left(new UndeclaredVariable(this.node.getIdentifier())));
                return e_ -> new Product<SymbolTable, Either<TypeError, DeclarationNode.Type>>(e_, Either.right(ty.get()));
            })
            .read(e);
    }
}

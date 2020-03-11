package plic.typechecker.checkers;

import data.either.Either;
import data.product.Product;
import plic.core.DeclarationNode;
import plic.core.IdentifierNode;
import plic.core.IntegerNode;
import plic.core.ValueNode;
import plic.typechecker.TypeCheck;
import plic.typechecker.core.SymbolTable;
import plic.typechecker.error.TypeError;

import static data.either.Either.left;

public class TCExpression implements TypeCheck<DeclarationNode.Type> {
    private ValueNode node;

    public TCExpression(ValueNode vn) {
        this.node = vn;
    }

    @Override
    public Product<SymbolTable, Either<TypeError, DeclarationNode.Type>> read(SymbolTable e) {
        if (this.node instanceof IdentifierNode) {
            return new TCIdentifier((IdentifierNode) this.node).read(e);
        } else if (this.node instanceof IntegerNode) {
            return new TCInteger((IntegerNode) this.node).read(e);
        }
        return new Product<>(e, left(new TypeError()));
    }
}

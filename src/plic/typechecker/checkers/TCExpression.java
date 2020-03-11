package plic.typechecker.checkers;

import data.either.Either;
import data.product.Product;
import plic.parser.ast.DeclarationNode;
import plic.parser.ast.IdentifierNode;
import plic.parser.ast.IntegerNode;
import plic.parser.ast.ValueNode;
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

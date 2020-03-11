package plic.typechecker.checkers;

import data.either.Either;
import data.product.Product;
import plic.parser.ast.DeclarationNode;
import plic.parser.ast.IntegerNode;
import plic.typechecker.TypeCheck;
import plic.typechecker.core.SymbolTable;
import plic.typechecker.error.TypeError;

import static data.either.Either.right;

public class TCInteger implements TypeCheck<DeclarationNode.Type> {
    private IntegerNode node;

    public TCInteger(IntegerNode in) {
        this.node = in;
    }

    @Override
    public Product<SymbolTable, Either<TypeError, DeclarationNode.Type>> read(SymbolTable e) {
        return new Product<>(e, right(DeclarationNode.Type.ENTIER));
    }
}

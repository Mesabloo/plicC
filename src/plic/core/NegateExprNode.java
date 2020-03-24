package plic.core;

import data.either.Either;
import data.product.Product;
import plic.typechecker.core.SymbolTable;
import plic.typechecker.core.Type;
import plic.typechecker.error.TypeError;

public class NegateExprNode extends ValueNode {
    private ValueNode node;

    public NegateExprNode(ValueNode vn) {
        this.node = vn;
    }

    @Override
    public StringBuilder generateMIPSAsLHS(StringBuilder builder, int indent) {
        return null;
    }

    @Override
    public StringBuilder generateMIPSAsRHS(StringBuilder builder, int indent) {
        return null;
    }

    @Override
    public StringBuilder generateMIPS(StringBuilder builder, int indent) {
        return null;
    }

    @Override
    public Product<SymbolTable, Either<TypeError, Type>> typecheck(SymbolTable s) {
        return null;
    }
}

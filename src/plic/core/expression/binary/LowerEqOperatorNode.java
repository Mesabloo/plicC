package plic.core.expression.binary;

import data.either.Either;
import data.product.Product;
import plic.typechecker.core.SymbolTable;
import plic.typechecker.core.Type;
import plic.typechecker.error.TypeError;

public class LowerEqOperatorNode extends BinaryOperatorNode {
    @Override
    public StringBuilder generateMIPSAsRHS(StringBuilder builder, int indent) {
        return generateRHSWith(builder, indent, "sle");
    }

    @Override
    public Product<SymbolTable, Either<TypeError, Type>> typecheck(SymbolTable s) {
        return check("<=").read(s);
    }

    @Override
    public String toString_(int level) {
        return e1.toString_(level) + " <= " + e2.toString_(level);
    }
}

package plic.core.expression.unary;

import data.either.Either;
import data.product.Product;
import plic.typechecker.core.SymbolTable;
import plic.typechecker.core.Type;
import plic.typechecker.error.TypeError;

public class NegateExprNode extends UnaryOperatorNode {
    @Override
    public StringBuilder generateMIPSAsRHS(StringBuilder builder, int indent) {
        return e1.generateMIPSAsRHS(builder, indent)
            .append(genIndent(indent))
                .append("neg $v0, $v0\n");
    }

    @Override
    public Product<SymbolTable, Either<TypeError, Type>> typecheck(SymbolTable s) {
        return check("-").read(s);
    }

    @Override
    public String toString_(int level) {
        return "- (" + e1.toString_(level) + ")";
    }
}

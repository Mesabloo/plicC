package plic.core.expression;

import data.either.Either;
import data.product.Product;
import plic.lexer.token.IntegerToken;
import plic.typechecker.core.SymbolTable;
import plic.typechecker.core.Type;
import plic.typechecker.error.TypeError;

public class IntegerNode extends ValueNode {
    private long val;

    public IntegerNode(IntegerToken v) {
        this.val = v.getLong();
    }

    @Override
    public String toString_(int level) {
        return "" + val;
    }

    public long getInteger() {
        return this.val;
    }

    @Override
    public Product<SymbolTable, Either<TypeError, Type>> typecheck(SymbolTable s) {
        return new Product<>(s, Either.right(Type.ENTIER));
    }

    @Override
    public StringBuilder generateMIPS(StringBuilder builder, int indent) {
        return builder
            .append(genIndent(indent))
                .append("li $v0, ")
                .append(val)
                .append("\n");
    }

    @Override
    public StringBuilder generateMIPSAsLHS(StringBuilder builder, int indent) {
        throw new IllegalStateException("An integer cannot be used as a left-hand-size expression!");
    }

    @Override
    public StringBuilder generateMIPSAsRHS(StringBuilder builder, int indent) {
        return generateMIPS(builder, indent);
    }
}

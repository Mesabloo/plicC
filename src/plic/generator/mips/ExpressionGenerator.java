package plic.generator.mips;

import plic.core.IntegerNode;
import plic.core.ValueNode;

public class ExpressionGenerator extends MIPSGenerator {
    private ValueNode node;

    public ExpressionGenerator(ValueNode vn) {
        this.node = vn;
    }

    @Override
    public StringBuilder generate(StringBuilder builder) {
        // We only handle integers for now
        if (node instanceof IntegerNode)
            return builder
                .append("li $v0, ")
                .append(((IntegerNode) node).getInteger())
                .append("\n");
        return builder;
    }
}

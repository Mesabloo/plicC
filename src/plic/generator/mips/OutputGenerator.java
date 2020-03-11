package plic.generator.mips;

import plic.core.OutputInstrNode;

public class OutputGenerator extends MIPSGenerator {
    private OutputInstrNode node;

    public OutputGenerator(OutputInstrNode oin) {
        this.node = oin;
    }

    @Override
    public StringBuilder generate(StringBuilder builder) {
        return new ExpressionGenerator(node.getExpression())
            .generate(builder)
            .append("println ()\n");
    }
}

package plic.parser.ast;

public class OutputInstrNode extends InstructionNode {
    private ValueNode val;

    public OutputInstrNode(ValueNode v) {
        this.val = v;
    }

    @Override
    public String toString_(int level) {
        String prefix = super.toString_(level);
        return prefix + "write " + this.val.toString_(level);
    }

    public ValueNode getExpression() {
        return this.val;
    }
}

package plic.parser.ast;

public class DefinitionNode extends InstructionNode {
    private ValueNode lhs;
    private ValueNode rhs;

    public DefinitionNode(ValueNode l, ValueNode r) {
        this.lhs = l;
        this.rhs = r;
    }

    @Override
    public String toString_(int level) {
        String prefix = super.toString_(level);
        return prefix + lhs.toString_(level) + " := " + rhs.toString_(level);
    }
}

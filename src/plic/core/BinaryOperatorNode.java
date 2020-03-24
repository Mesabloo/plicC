package plic.core;

public abstract class BinaryOperatorNode extends ValueNode {
    protected ValueNode e1;
    protected ValueNode e2;

    public BinaryOperatorNode set(ValueNode v1, ValueNode v2) {
        this.e1 = v1;
        this.e2 = v2;
        return this;
    }
}

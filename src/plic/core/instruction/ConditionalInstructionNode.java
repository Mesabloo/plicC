package plic.core.instruction;

public abstract class ConditionalInstructionNode extends InstructionNode {
    public abstract StringBuilder generateElseBranchIfNeeded(int indent);

    public abstract StringBuilder generateElse(int indent);
}

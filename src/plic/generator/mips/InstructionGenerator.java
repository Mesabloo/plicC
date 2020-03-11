package plic.generator.mips;

import plic.core.DeclarationNode;
import plic.core.DefinitionNode;
import plic.core.InstructionNode;
import plic.core.OutputInstrNode;

public class InstructionGenerator extends MIPSGenerator {
    private InstructionNode node;

    public InstructionGenerator(InstructionNode in) {
        this.node = in;
    }

    @Override
    public StringBuilder generate(StringBuilder builder) {
        if (node instanceof DeclarationNode)
            return new DeclarationGenerator((DeclarationNode) node).generate(builder);
        if (node instanceof DefinitionNode)
            return new DefinitionGenerator((DefinitionNode) node).generate(builder);
        if (node instanceof OutputInstrNode)
            return new OutputGenerator((OutputInstrNode) node).generate(builder);
        return builder;
    }
}

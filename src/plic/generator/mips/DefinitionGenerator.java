package plic.generator.mips;

import plic.core.DefinitionNode;
import plic.core.IdentifierNode;

public class DefinitionGenerator extends MIPSGenerator {
    private DefinitionNode node;

    public DefinitionGenerator(DefinitionNode dn) {
        this.node = dn;
    }

    @Override
    public StringBuilder generate(StringBuilder builder) {
        builder = new ExpressionGenerator(node.rhs()).generate(builder);
        // For now, left-hand-sides are only identifiers, but just in case, let's just check
        if (node.lhs() instanceof IdentifierNode)
            return builder
                .append("sw $v0, __")
                .append(((IdentifierNode) node.lhs()).getIdentifier())
                .append("\n");
        return builder;
    }
}

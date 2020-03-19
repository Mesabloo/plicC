package plic.core;

import plic.generator.MIPSGenerator;
import plic.typechecker.TypeCheck;
import plic.typechecker.core.Type;

public abstract class ValueNode extends TreeNode implements TypeCheck<Type>, MIPSGenerator {
    public abstract StringBuilder generateMIPSAsLHS(StringBuilder builder, int indent);
    public abstract StringBuilder generateMIPSAsRHS(StringBuilder builder, int indent);
}

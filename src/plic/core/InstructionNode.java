package plic.core;

import plic.generator.MIPSGenerator;
import plic.typechecker.TypeCheck;
import plic.typechecker.core.SymbolTable;

public abstract class InstructionNode extends TreeNode implements TypeCheck<SymbolTable>, MIPSGenerator {
}

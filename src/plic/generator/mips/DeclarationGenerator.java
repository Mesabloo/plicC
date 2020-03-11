package plic.generator.mips;

import plic.core.DeclarationNode;
import plic.typechecker.core.SymbolTable;

public class DeclarationGenerator extends MIPSGenerator {
    private DeclarationNode node;

    public DeclarationGenerator(DeclarationNode dn) {
        this.node = dn;
    }

    @Override
    public StringBuilder generate(StringBuilder builder) {
        return builder.append("# declaring ")
            .append(this.node.getName())
            .append("\n")
            .append(".eqv __")
            .append(this.node.getName())
            .append(" ")
            .append(MIPSGenerator.symbols.getOffsetOf(this.node.getName()))
            .append("$s0\n")
            .append("addi $sp, $sp, ")
            .append(SymbolTable.TypeSize.fromType(MIPSGenerator.symbols.getTypeOf(this.node.getName()).get()).size());
    }
}

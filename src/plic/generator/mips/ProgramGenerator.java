package plic.generator.mips;

import plic.core.ProgramNode;

public class ProgramGenerator extends MIPSGenerator {
    private ProgramNode node;

    public ProgramGenerator(ProgramNode pn) {
        this.node = pn;
    }

    @Override
    public StringBuilder generate(StringBuilder builder) {
        return new BlockGenerator(this.node.getStatements())
            .generate(builder
                .append(".text\n# programme ")
                .append(this.node.getIdentifier().getIdentifier())
                .append("\n")
                .append(printMacro)
                .append("\nmain:\n")
                .append("move $s0, $sp\n"))
            .append(".data\n")
            .append("nl: .asciiz \"\\n\"\n");
    }

    private static StringBuilder printMacro = new StringBuilder()
        .append(".macro println ()\n")
        .append("# Print $a0\n")
        .append("li $v0, 1\n")
        .append("syscall\n")
        .append("# Print \"\\n\"\n")
        .append("li $v0, 4\n")
        .append("la $a0, nl\n")
        .append("syscall\n")
        .append("xor $v0, $v0, $v0\n")
        .append("xor $a0, $a0, $a0\n")
        .append(".end_macro\n");
}

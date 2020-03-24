package plic.core;

import data.either.Either;
import data.product.Product;
import plic.generator.MIPSGenerator;
import plic.typechecker.TypeCheck;
import plic.typechecker.core.SymbolTable;
import plic.typechecker.error.TypeError;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ProgramNode extends BlockNode implements TypeCheck<Void> {
    private IdentifierNode name;

    public ProgramNode(IdentifierNode n, List<InstructionNode> ss) {
        super(ss);
        this.name = n;
    }

    @Override
    public String toString_(int level) {
        String prefix = super.toString_(level);
        return prefix + "Program '" + name + "'\n" + super.toList().stream().map(node -> node.toString_(level + INDENT)).collect(Collectors.joining("\n"));
    }

    @Override
    public Product<SymbolTable, Either<TypeError, Void>> typecheck(SymbolTable s) {
        return super.typecheck(s);
    }

    @Override
    public StringBuilder generateMIPS(StringBuilder builder, int indent) {
        return super.generateMIPS(
            builder
                .append("# programme ")
                    .append(name.getIdentifier())
                    .append("\n")
                .append(".text\n")
                .append(print)
                .append("\n")
                .append(push)
                .append("\n")
                .append(pop)
                .append("\n")
                .append(boundCheck)
                .append("\n\n")
                .append(genIndent(indent))
                    .append("main:\n")
                .append("# Initialise the stack for later\n")
                .append(genIndent(indent + 4))
                    .append("move $s0, $sp\n"),
            indent
        )
            .append(genIndent(indent + 4))
                .append("li $a0, 0\n")
            .append("\n\n")
            .append(genIndent(indent))
                .append("exit:\n")
            .append("# We can exit now...\n")
            .append(genIndent(indent + 4))
                .append("li $v0, 17\n")
            .append(genIndent(indent + 4))
                .append("syscall\n")
            .append(arrayIndexOutOfBounds)
            .append("\n\n")
            .append(genIndent(indent))
                .append(".data\n")
            .append(genIndent(indent))
                .append("nl: .asciiz \"\\n\"\n")
            .append(genIndent(indent))
                .append("arrayIndexOutOfBoundsMessage: .asciiz \"ERREUR: Array index out of bounds: \"\n")
            .append(genIndent(indent))
                .append("arrayMaxSizeMessage: .asciiz \"array size: \"\n")
            .append(genIndent(indent))
                .append("arrayIndexAtMessage: .asciiz \"; index: \"\n");
    }

    private static StringBuilder print = new StringBuilder()
        .append("# prints the content of %1\n")
        .append(".macro println (%1)\n")
        .append("# Print $a0\n")
        .append("    move $a0, %1\n")
        .append("    li $v0, 1\n")
        .append("    syscall\n")
        .append("# Print \"\\n\"\n")
        .append("    li $v0, 4\n")
        .append("    la $a0, nl\n")
        .append("    syscall\n")
        .append(".end_macro\n");

    private static StringBuilder push = new StringBuilder()
        .append("# pushes %1 of size %2 onto the stack\n")
        .append(".macro push (%1, %2)\n")
        .append("    sw %1, ($sp)\n")
        .append("    subu $sp, $sp, %2\n")
        .append(".end_macro\n");

    private static StringBuilder pop = new StringBuilder()
        .append("# pops the stack by %2 octets into %1\n")
        .append(".macro pop (%1, %2)\n")
        .append("    addu $sp, $sp, %2\n")
        .append("    lw %1, ($sp)\n")
        .append(".end_macro\n");

    private static StringBuilder boundCheck = new StringBuilder()
        .append("# checks if 0 <= %1 < %2 else throws an ArrayIndexOutOfBoundsException\n")
        .append(".macro boundCheck (%1, %2)\n")
        .append("    li $t0, %2\n")
        .append("# if %1 >= $t0 goto [arrayIndexOutOfBoundsException]\n")
        .append("    bge %1, $t0, arrayIndexOutOfBoundsException\n")
        .append("    li $t1, 0\n")
        .append("# if %1 < 0 goto [arrayIndexOutOfBoundsException]\n")
        .append("    blt %1, $t1, arrayIndexOutOfBoundsException\n")
        .append(".end_macro");


    private static StringBuilder arrayIndexOutOfBounds = new StringBuilder()
        .append("\n\narrayIndexOutOfBoundsException:\n")
        .append("    move $t1, $v0\n")
        .append("    li $v0, 4\n")
        .append("    la $a0, arrayIndexOutOfBoundsMessage\n")
        .append("    syscall\n")
        .append("    la $a0, arrayMaxSizeMessage\n")
        .append("    syscall\n")
        .append("    move $a0, $t0\n")
        .append("    li $v0, 1\n")
        .append("    syscall\n")
        .append("    la $a0, arrayIndexAtMessage\n")
        .append("    li $v0, 4\n")
        .append("    syscall\n")
        .append("    println ($t1)\n")
        .append("    li $a0, 1\n")
        .append("    j exit\n");
}

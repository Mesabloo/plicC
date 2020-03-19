package plic.core;

import data.either.Either;
import data.product.Product;
import plic.generator.MIPSGenerator;
import plic.typechecker.TypeCheck;
import plic.typechecker.core.SymbolTable;
import plic.typechecker.error.TypeError;

public class SyntaxTree implements TypeCheck<Void>, MIPSGenerator {
    private ProgramNode root;

    public SyntaxTree(ProgramNode main) {
        this.root = main;
    }

    @Override
    public String toString() {
        return root.toString_(0);
    }

    public ProgramNode getRoot() {
        return this.root;
    }

    @Override
    public Product<SymbolTable, Either<TypeError, Void>> typecheck(SymbolTable s) {
        return root.typecheck(s);
    }

    @Override
    public StringBuilder generateMIPS(StringBuilder builder, int indent) {
        return root.generateMIPS(builder, 0);
    }
}

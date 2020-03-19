package plic.generator;

import plic.typechecker.core.SymbolTable;

import java.util.Collections;

public interface MIPSGenerator extends Generator {
    SymbolTable symbols = SymbolTable.fromNodes(Collections.emptyList());

    @Override
    default StringBuilder generate(StringBuilder builder, int n) {
        return generateMIPS(builder, n);
    }

    StringBuilder generateMIPS(StringBuilder builder, int indent);
}

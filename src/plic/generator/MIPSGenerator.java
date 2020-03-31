package plic.generator;

import plic.typechecker.core.SymbolTable;

import java.util.Collections;

public interface MIPSGenerator extends Generator {
    SymbolTable symbols = SymbolTable.fromNodes(Collections.emptyList());
    LongWrapper blockCounter = new LongWrapper();

    class LongWrapper {
        private Long member;

        public LongWrapper() {
            member = 0L;
        }
        public LongWrapper(long l) {
            member = l;
        }

        public long preIncrement(long incr) {
            return member += incr;
        }

        public long get() {
            return member;
        }
    }

    @Override
    default StringBuilder generate(StringBuilder builder, int n) {
        return generateMIPS(builder, n);
    }

    StringBuilder generateMIPS(StringBuilder builder, int indent);
}

package plic.parser.ast;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class SymbolTable {
    public static class Key {
        private String val;

        public Key(String k) {
            this.val = k;
        }

        @Override
        public int hashCode() {
            return val.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof Key && ((Key) obj).val.equals(this.val);
        }
    }
    public static class Entry {
        private DeclarationNode.Type type;
        private long off;

        public Entry(DeclarationNode.Type ty, long offset) {
            this.type = ty;
            this.off = offset;
        }
    }

    public enum TypeSize {
        ENTIER(4);

        private long size;

        TypeSize(long s) {
            this.size = s;
        }

        public long size() {
            return this.size;
        }

        public static TypeSize fromType(DeclarationNode.Type ty) {
            switch (ty) {
                case ENTIER:
                    return ENTIER;
                default:
                    throw new IllegalArgumentException("Not yet implemented!");
            }
        }
    }

    private LinkedHashMap<Key, Entry> symbols;
    private long pointer;

    private SymbolTable() {
        this.symbols = new LinkedHashMap<>();
        pointer = 0;
    }

    public void add(String k, DeclarationNode.Type ty) {
        TypeSize ts = TypeSize.fromType(ty);
        pointer += ts.size();
        this.symbols.put(new Key(k), new Entry(ty, pointer));
    }

    public void remove(String k) {
        this.symbols.remove(new Key(k));
    }

    public DeclarationNode.Type getTypeOf(String identifier) {
        return this.symbols.get(new Key(identifier)).type;
    }

    public long getOffsetOf(String identifier) {
        return this.symbols.get(new Key(identifier)).off;
    }

    public static SymbolTable fromNodes(ArrayList<DeclarationNode> stts) {
        SymbolTable st = new SymbolTable();
        stts.forEach(n -> st.add(n.getName(), n.getType()));
        return st;
    }
}

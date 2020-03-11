package plic.typechecker.core;

import control.copy.Copiable;
import plic.parser.ast.DeclarationNode;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class SymbolTable implements Copiable<SymbolTable> {
    public static class Key implements Copiable<Key> {
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

        @Override
        public Key copy() {
            return new Key(new String(val));
        }
    }
    public static class Entry implements Copiable<Entry> {
        private DeclarationNode.Type type;
        private long off;

        public Entry(DeclarationNode.Type ty, long offset) {
            this.type = ty;
            this.off = offset;
        }

        @Override
        public Entry copy() {
            return new Entry(type, off);
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
        this.symbols.put(new Key(k), new Entry(ty, pointer));
        pointer -= ts.size();
    }

    public void remove(String k) {
        this.symbols.remove(new Key(k));
    }

    public Optional<DeclarationNode.Type> getTypeOf(String identifier) {
        return Optional.ofNullable(this.symbols.get(new Key(identifier))).map(e -> e.type);
    }

    public Optional<Long> getOffsetOf(String identifier) {
        return Optional.ofNullable(this.symbols.get(new Key(identifier))).map(e -> e.off);
    }

    public static SymbolTable fromNodes(ArrayList<DeclarationNode> stts) {
        SymbolTable st = new SymbolTable();
        stts.forEach(n -> st.add(n.getName(), n.getType()));
        return st;
    }

    @Override
    public SymbolTable copy() {
        SymbolTable st = new SymbolTable();
        for (Map.Entry<Key, Entry> e : this.symbols.entrySet())
            st.add(e.getKey().copy().val, e.getValue().copy().type);
        return st;
    }
}

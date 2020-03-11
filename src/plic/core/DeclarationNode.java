package plic.parser.ast;

import data.either.Either;
import data.product.Product;
import plic.parser.Parsec;
import plic.parser.stream.Reader;

public class DeclarationNode extends InstructionNode {
    public enum Type {
        ENTIER, // TABLEAU
            ;

        @Override
        public String toString() {
            switch (this) {
                case ENTIER: return "integer";
                default:
                    throw new IllegalArgumentException();
            }
        }
    }
    private String name;
    private Type type;

    public DeclarationNode(Type ty, String n) {
        this.type = ty;
        this.name = n;
    }

    @Override
    public String toString_(int level) {
        String prefix = super.toString_(level);
        return prefix + "let " + name + " : " + type.toString();
    }

    public String getName() {
        return this.name;
    }

    public Type getType() {
        return this.type;
    }
}

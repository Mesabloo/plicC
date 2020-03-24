package plic.core.instruction;

import control.monad.state.MonadState;
import data.either.Either;
import data.product.Product;
import plic.typechecker.core.SymbolTable;
import plic.typechecker.error.RedefinedVariable;
import plic.typechecker.error.TypeError;

import java.util.Optional;

public class DeclarationNode extends InstructionNode {
    public static abstract class Type {
        public abstract SymbolTable.TypeSize getTypeSize();
        public abstract plic.typechecker.core.Type toType();
    }
    public static class TypeEntier extends Type {
        @Override
        public SymbolTable.TypeSize getTypeSize() {
            return SymbolTable.TypeSize.ENTIER;
        }

        @Override
        public plic.typechecker.core.Type toType() {
            return plic.typechecker.core.Type.ENTIER;
        }

        @Override
        public String toString() {
            return "integer";
        }
    }
    public static class TypeTableau extends Type {
        private int size;

        public TypeTableau(int s) {
            this.size = s;
        }

        public int len() {
            return this.size;
        }

        @Override
        public SymbolTable.TypeSize getTypeSize() {
            return SymbolTable.TypeSize.TABLEAU.apply((long) this.size);
        }

        @Override
        public plic.typechecker.core.Type toType() {
            return plic.typechecker.core.Type.TABLEAU;
        }

        @Override
        public String toString() {
            return "array[" + this.size + "]";
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

    @Override
    public Product<SymbolTable, Either<TypeError, SymbolTable>> typecheck(SymbolTable s) {
        return get()
            .bind(env -> {
                Optional<Type> ty = env.getTypeOf(this.name);
                if (ty.isPresent())
                    return e_ -> new Product<SymbolTable, Either<TypeError, SymbolTable>>(e_, Either.left(new RedefinedVariable(this.name)));
                env.add(this.name, this.type);
                return MonadState.pure(Either.right(env));
            })
            .read(s);
    }

    @Override
    public StringBuilder generateMIPS(StringBuilder builder, int indent) {
        return builder
            .append("# ")
                .append(this.type.toString())
                .append(" ")
                .append(name)
                .append("\n")
            .append(genIndent(indent))
                .append(".eqv __var_")
                    .append(this.name)
                    .append(" ")
                    .append(symbols.getOffsetOf(name).get()) // SAFE: checked when typechecking
                    .append("($s0)\n")
            .append(genIndent(indent))
                .append("subi $sp, $sp, ")
                    .append(symbols.getTypeOf(name).get().getTypeSize().size())
                    .append("\n");
    }
}

package plic.core;

import data.either.Either;
import data.product.Product;
import plic.lexer.token.IdentifierToken;
import plic.typechecker.core.SymbolTable;
import plic.typechecker.core.Type;
import plic.typechecker.error.TypeError;
import plic.typechecker.error.UndeclaredVariable;

import java.util.Optional;

public class IdentifierNode extends ValueNode {
    private String name;

    public IdentifierNode(IdentifierToken tk) {
        this.name = tk.getName();
    }

    @Override
    public String toString_(int level) {
        return name;
    }

    public String getIdentifier() {
        return name;
    }

    @Override
    public Product<SymbolTable, Either<TypeError, Type>> typecheck(SymbolTable s) {
        return get()
            .bind(env -> {
                Optional<DeclarationNode.Type> ty = env.getTypeOf(this.name);
                if (!ty.isPresent())
                    return e_ -> new Product<>(e_, Either.left(new UndeclaredVariable(this.name)));
                return e_ -> new Product<SymbolTable, Either<TypeError, Type>>(e_, Either.right(ty.get().toType()));
            })
            .read(s);
    }

    @Override
    public StringBuilder generateMIPS(StringBuilder builder, int indent) {
        return builder
            .append(genIndent(indent))
                .append("lw $v0, __var_")
                    .append(name)
                    .append("\n");
    }

    @Override
    public StringBuilder generateMIPSAsLHS(StringBuilder builder, int indent) {
        return builder
            .append(genIndent(indent))
                .append("la $a0, __var_")
                    .append(symbols.getOffsetOf(name).get())
                    .append("\n");
    }

    @Override
    public StringBuilder generateMIPSAsRHS(StringBuilder builder, int indent) {
        return generateMIPS(builder, indent);
    }
}

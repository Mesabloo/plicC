package plic.core;

import data.either.Either;
import data.product.Product;
import plic.typechecker.core.SymbolTable;
import plic.typechecker.core.Type;
import plic.typechecker.error.CannotUnifyTypes;
import plic.typechecker.error.TypeError;

public class AccessNode extends ValueNode {
    private IdentifierNode id;
    private ValueNode offset;

    public AccessNode(IdentifierNode node, ValueNode off) {
        this.id = node;
        this.offset = off;
    }

    @Override
    public String toString_(int level) {
        return id.toString_(level) + "[" + offset.toString_(level) + "]";
    }

    public String getIdentifier() {
        return this.id.getIdentifier();
    }

    public ValueNode getOffset() {
        return this.offset;
    }

    @Override
    public Product<SymbolTable, Either<TypeError, Type>> typecheck(SymbolTable s) {
        return id.fmap(tyErr ->
                tyErr.bind(ty -> ty == Type.TABLEAU
                    ? Either.right(Type.ENTIER)
                    : Either.left(new CannotUnifyTypes(Type.TABLEAU, ty))
                )
            )
            .read(s);
    }

    @Override
    public StringBuilder generateMIPS(StringBuilder builder, int indent) {
        return generateMIPSAsRHS(builder, indent);
    }

    @Override
    public StringBuilder generateMIPSAsLHS(StringBuilder builder, int indent) {
        return offset.generateMIPSAsRHS(builder, indent)
            .append(genIndent(indent))
                .append("li $t1, ")
                    .append(((DeclarationNode.TypeTableau) symbols.getTypeOf(id.getIdentifier()).get()).len())
                    .append("\n")
            .append(genIndent(indent))
                .append("bge $v0, $t1, arrayIndexOutOfBoundsException\n")
            .append("# si (index >= len(")
                .append(id.getIdentifier())
                .append(") alors throw ArrayIndexOutOfBoundsException\n")
            .append(genIndent(indent))
                .append("li $t1, 0\n")
            .append(genIndent(indent))
                .append("blt $v0, $t1, arrayIndexOutOfBoundsException\n")
            .append("# si (index < 0) alors throw ArrayIndexOutOfBoundsException\n")
            .append(genIndent(indent))
                .append("move $a0, $s0\n")
            .append(genIndent(indent))
                .append("addu $a0, $a0, ")
                    .append(symbols.getOffsetOf(id.getIdentifier()).get())
                    .append("\n")
            .append(genIndent(indent))
                .append("mulu $v0, $v0, 4\n") // we only handle integer arrays so it's ok to put 4 here.
            .append(genIndent(indent))
                .append("subu $a0, $a0, $v0\n");
    }

    @Override
    public StringBuilder generateMIPSAsRHS(StringBuilder builder, int indent) {
        return offset.generateMIPSAsRHS(builder, indent)
            .append(genIndent(indent))
                .append("li $t1, ")
                    .append(((DeclarationNode.TypeTableau) symbols.getTypeOf(id.getIdentifier()).get()).len())
                    .append("\n")
            .append(genIndent(indent))
                .append("bge $v0, $t1, arrayIndexOutOfBoundsException\n")
            .append("# si (index >= len(")
                .append(id.getIdentifier())
                .append(") alors throw ArrayIndexOutOfBoundsException\n")
            .append(genIndent(indent))
                .append("li $t1, 0\n")
            .append(genIndent(indent))
                .append("blt $v0, $t1, arrayIndexOutOfBoundsException\n")
            .append("# si (index < 0) alors throw ArrayIndexOutOfBoundsException\n")
            .append(genIndent(indent))
                .append("move $t0, $s0\n")
            .append(genIndent(indent))
                .append("addu $t0, $t0, ")
                    .append(symbols.getOffsetOf(id.getIdentifier()).get())
                    .append("\n")
            .append(genIndent(indent))
                .append("mulu $v0, $v0, 4\n") // we only handle integer arrays so it's ok to put 4 here.
            .append(genIndent(indent))
                .append("subu $t0, $t0, $v0\n")
            .append(genIndent(indent))
                .append("lw $v0, ($t0)\n");
    }
}
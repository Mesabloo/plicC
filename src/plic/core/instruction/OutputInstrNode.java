package plic.core.instruction;

import data.either.Either;
import data.product.Product;
import plic.core.expression.ValueNode;
import plic.typechecker.core.SymbolTable;
import plic.typechecker.core.Type;
import plic.typechecker.error.CannotUnifyTypes;
import plic.typechecker.error.TypeError;

import static data.either.Either.left;
import static data.either.Either.right;

public class OutputInstrNode extends InstructionNode {
    private ValueNode val;

    public OutputInstrNode(ValueNode v) {
        this.val = v;
    }

    @Override
    public String toString_(int level) {
        String prefix = super.toString_(level);
        return prefix + "write " + this.val.toString_(level);
    }

    public ValueNode getExpression() {
        return this.val;
    }

    @Override
    public Product<SymbolTable, Either<TypeError, SymbolTable>> typecheck(SymbolTable s) {
        return get()
            .bind(env -> {
                Either<TypeError, Type> res = this.val.typecheck(env).snd;
                if (res.isLeft())
                    return e_ -> new Product<>(e_, left(res.fromLeft()));
                Type t = res.fromRight();

                if (t != Type.ENTIER && t != Type.BOOLEEN)
                    return e_ -> new Product<>(e_, left(new CannotUnifyTypes(t, Type.ENTIER)));

                return e_ -> new Product<SymbolTable, Either<TypeError, SymbolTable>>(e_, right(env));
            })
            .read(s);
    }

    @Override
    public StringBuilder generateMIPS(StringBuilder builder, int indent) {
        return val
            .generateMIPSAsRHS(builder
                .append("# print ")
                    .append(val.toString_(0))
                    .append("\n"),
                indent
            )
            .append(genIndent(indent))
                .append("println ($v0)\n");
    }
}

package plic.typechecker.checkers;

import data.either.Either;
import data.product.Product;
import plic.core.DeclarationNode;
import plic.core.OutputInstrNode;
import plic.typechecker.TypeCheck;
import plic.typechecker.core.SymbolTable;
import plic.typechecker.error.TypeError;

import static data.either.Either.left;
import static data.either.Either.right;

public class TCOutput implements TypeCheck<SymbolTable> {
    private OutputInstrNode node;

    public TCOutput(OutputInstrNode oin) {
        this.node = oin;
    }

    @Override
    public Product<SymbolTable, Either<TypeError, SymbolTable>> read(SymbolTable e) {
        return get()
            .bind(env -> {
                Either<TypeError, DeclarationNode.Type> res = new TCExpression(this.node.getExpression()).read(env).snd;
                if (res.isLeft())
                    return e_ -> new Product<>(e_, left(res.fromLeft()));
                return e_ -> new Product<SymbolTable, Either<TypeError, SymbolTable>>(e_, right(env));
            })
            .read(e);
    }
}

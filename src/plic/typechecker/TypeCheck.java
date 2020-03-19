package plic.typechecker;

import control.monad.state.MonadState;
import data.either.Either;
import data.product.Product;
import plic.typechecker.core.SymbolTable;
import plic.typechecker.error.TypeError;

public interface TypeCheck<Ty> extends MonadState<SymbolTable, Either<TypeError, Ty>> {
    @Override
    default Product<SymbolTable, Either<TypeError, Ty>> read(SymbolTable e) {
        return this.typecheck(e);
    }

    Product<SymbolTable, Either<TypeError, Ty>> typecheck(SymbolTable s);
}

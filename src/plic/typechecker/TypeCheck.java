package plic.typechecker;

import control.monad.state.MonadState;
import data.either.Either;
import data.product.Product;
import plic.typechecker.core.SymbolTable;
import plic.typechecker.error.TypeError;

public interface TypeCheck<Ty> extends MonadState<SymbolTable, Either<TypeError, Ty>> {
    @Override
    Product<SymbolTable, Either<TypeError, Ty>> read(SymbolTable e);
}

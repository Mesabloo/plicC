package plic.typechecker;

import data.either.Either;
import data.product.Product;
import plic.core.ProgramNode;
import plic.core.SyntaxTree;
import plic.typechecker.core.SymbolTable;
import plic.typechecker.error.TypeCheckerError;
import plic.typechecker.error.TypeError;

import java.util.ArrayList;
import java.util.Collections;

public class TypeChecker {
    public SymbolTable check(SyntaxTree tree) {
        Product<SymbolTable, Either<TypeError, Void>> res = tree.typecheck(SymbolTable.fromNodes(Collections.emptyList()));
        if (res.snd.isLeft())
            throw new TypeCheckerError(res.snd.fromLeft());
        return res.fst;
    }
}

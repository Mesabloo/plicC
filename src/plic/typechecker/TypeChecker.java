package plic.typechecker;

import data.either.Either;
import data.product.Product;
import plic.parser.ast.ProgramNode;
import plic.parser.ast.SyntaxTree;
import plic.typechecker.checkers.TCProgram;
import plic.typechecker.core.SymbolTable;
import plic.typechecker.error.TypeCheckerError;
import plic.typechecker.error.TypeError;

import java.util.ArrayList;

public class TypeChecker {
    public SymbolTable check(SyntaxTree tree) {
        Product<SymbolTable, Either<TypeError, Void>> res = new TCProgram((ProgramNode) tree.getRoot()).read(SymbolTable.fromNodes(new ArrayList<>()));
        if (res.snd.isLeft())
            throw new TypeCheckerError(res.snd.fromLeft());
        return res.fst;
    }
}

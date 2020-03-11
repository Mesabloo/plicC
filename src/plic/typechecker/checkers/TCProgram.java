package plic.typechecker.checkers;

import data.either.Either;
import data.product.Product;
import plic.core.InstructionNode;
import plic.core.ProgramNode;
import plic.core.TreeNode;
import plic.typechecker.TypeCheck;
import plic.typechecker.core.SymbolTable;
import plic.typechecker.error.TypeError;

import java.util.ArrayList;

public class TCProgram implements TypeCheck<Void> {
    private ProgramNode node;

    public TCProgram(ProgramNode n) {
        this.node = n;
    }

    @Override
    public Product<SymbolTable, Either<TypeError, Void>> read(SymbolTable e) {
        ArrayList<TreeNode> stts = (ArrayList<TreeNode>) this.node.getStatements().clone();
        if (stts.isEmpty())
            return new Product<>(e, Either.right(null));
        else {
            return new TCInstructionNode((InstructionNode) stts.remove(0))
                .bind(newEnv -> {
                    if (newEnv.isLeft())
                        return e_ -> new Product<>(e_, Either.left(newEnv.fromLeft()));
                    SymbolTable st = newEnv.fromRight();
                    return new TCProgram(new ProgramNode(node.getIdentifier(), stts)).local(e_ -> st);
                })
                .read(e);
        }
    }
}

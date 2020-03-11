package plic.typechecker.checkers;

import data.either.Either;
import data.product.Product;
import plic.core.*;
import plic.typechecker.TypeCheck;
import plic.typechecker.core.SymbolTable;
import plic.typechecker.error.TypeError;

public class TCInstructionNode implements TypeCheck<SymbolTable> {
    private InstructionNode tn;

    public TCInstructionNode(InstructionNode node) {
        this.tn = node;
    }

    @Override
    public Product<SymbolTable, Either<TypeError, SymbolTable>> read(SymbolTable e) {
        if (this.tn instanceof DeclarationNode) {
            return new TCDeclaration((DeclarationNode) this.tn).read(e);
        } else if (this.tn instanceof DefinitionNode) {
            return new TCDefinition((DefinitionNode) this.tn).read(e);
        } else if (this.tn instanceof OutputInstrNode) {
            return new TCOutput((OutputInstrNode) this.tn).read(e);
        }
        return new Product<>(e, Either.left(new TypeError()));
    }
}

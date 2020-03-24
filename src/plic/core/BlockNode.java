package plic.core;

import control.monad.state.MonadState;
import data.either.Either;
import data.product.Product;
import plic.core.instruction.InstructionNode;
import plic.generator.MIPSGenerator;
import plic.typechecker.TypeCheck;
import plic.typechecker.core.SymbolTable;
import plic.typechecker.error.TypeError;

import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class BlockNode extends TreeNode implements TypeCheck<Void>, MIPSGenerator {
    private List<InstructionNode> statements;

    public BlockNode(List<InstructionNode> nodes) {
        this.statements = nodes;
    }

    public List<InstructionNode> toList() {
        return this.statements;
    }

    @Override
    public String toString_(int level) {
        return "{\n" + statements.stream().map(t -> t.toString_(level + INDENT)).collect(Collectors.joining("\n")) + "\n}";
    }

    @Override
    public Product<SymbolTable, Either<TypeError, Void>> typecheck(SymbolTable s) {
        BiFunction<InstructionNode, List<InstructionNode>, MonadState<SymbolTable, Either<TypeError, Void>>> tc =
            (node, tree) -> node
                .bind(env -> env.isLeft()
                    ? s_ -> new Product<>(s_, Either.left(env.fromLeft()))
                    : new BlockNode(tree).local(oldEnv -> env.fromRight())
                );
        return statements.isEmpty()
            ? new Product<>(s, Either.right(null))
            : tc.apply(statements.get(0), statements.subList(1, statements.size())).read(s) ;
    }

    @Override
    public StringBuilder generateMIPS(StringBuilder builder, int indent) {
        builder.append("# {\n");
        statements.forEach(s -> s.generateMIPS(builder, indent + 4));
        builder.append("# }\n");
        return builder;
    }
}

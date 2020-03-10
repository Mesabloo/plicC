package plic.parser.ast;

import data.product.Product;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class BlockNode extends TreeNode {
    private SymbolTable symbols;
    private ArrayList<TreeNode> statements;

    public BlockNode(Product<ArrayList<DeclarationNode>, ArrayList<TreeNode>> nodes) {
        this.symbols = SymbolTable.fromNodes(nodes.fst);
        this.statements = nodes.snd;
    }

    public ArrayList<TreeNode> toList() {
        return this.statements;
    }

    @Override
    public String toString_(int level) {
        return "{\n" + statements.stream().map(t -> t.toString_(level + INDENT)).collect(Collectors.joining("\n")) + "\n}";
    }
}

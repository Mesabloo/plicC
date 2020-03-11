package plic.core;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class BlockNode extends TreeNode {
    private ArrayList<TreeNode> statements;

    public BlockNode(ArrayList<TreeNode> nodes) {
        this.statements = nodes;
    }

    public ArrayList<TreeNode> toList() {
        return this.statements;
    }

    @Override
    public String toString_(int level) {
        return "{\n" + statements.stream().map(t -> t.toString_(level + INDENT)).collect(Collectors.joining("\n")) + "\n}";
    }
}

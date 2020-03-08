package plic.parser.ast;

public class SyntaxTree {
    private TreeNode root;

    public SyntaxTree(TreeNode main) {
        this.root = main;
    }

    @Override
    public String toString() {
        return root.toString_(0);
    }
}

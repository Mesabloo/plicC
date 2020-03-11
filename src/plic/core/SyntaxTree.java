package plic.core;

public class SyntaxTree {
    private TreeNode root;

    public SyntaxTree(TreeNode main) {
        this.root = main;
    }

    @Override
    public String toString() {
        return root.toString_(0);
    }

    public TreeNode getRoot() {
        return this.root;
    }
}

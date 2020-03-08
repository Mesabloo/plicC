package plic.parser.ast;

import data.either.Either;
import data.product.Product;
import plic.parser.Parsec;
import plic.parser.parsecs.BlockParsec;
import plic.parser.parsecs.IdentifierParsec;
import plic.parser.parsecs.KeywordParsec;
import plic.parser.stream.Reader;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class ProgramNode extends TreeNode {
    private ArrayList<TreeNode> statements;
    private IdentifierNode name;

    public ProgramNode(IdentifierNode n, ArrayList<TreeNode> ss) {
        this.name = n;
        this.statements = ss;
    }

    @Override
    public String toString_(int level) {
        String prefix = super.toString_(level);
        return prefix + "Program '" + name + "'\n" + statements.stream().map(node -> node.toString_(level + INDENT)).collect(Collectors.joining("\n"));
    }
}

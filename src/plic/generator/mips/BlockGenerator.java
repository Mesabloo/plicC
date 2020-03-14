package plic.generator.mips;

import plic.core.InstructionNode;
import plic.core.TreeNode;

import java.util.ArrayList;

public class BlockGenerator extends MIPSGenerator {
    private ArrayList<TreeNode> nodes;

    public BlockGenerator(ArrayList<TreeNode> tns) {
        this.nodes = tns;
    }

    @Override
    public StringBuilder generate(StringBuilder builder) {
        for (TreeNode node : nodes) {
            new InstructionGenerator((InstructionNode) node).generate(builder);
        }
        return builder;
    }
}

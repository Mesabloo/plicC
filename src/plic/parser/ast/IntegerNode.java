package plic.parser.ast;

import plic.lexer.token.IntegerToken;

public class IntegerNode extends ValueNode {
    private long val;

    public IntegerNode(IntegerToken v) {
        this.val = v.getLong();
    }

    @Override
    public String toString_(int level) {
        return "" + val;
    }
}

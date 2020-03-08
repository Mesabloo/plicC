package plic.parser.ast;

import plic.lexer.token.IdentifierToken;

public class IdentifierNode extends ValueNode {
    private String name;

    public IdentifierNode(IdentifierToken tk) {
        this.name = tk.getName();
    }

    @Override
    public String toString_(int level) {
        return name;
    }

    public String getIdentifier() {
        return name;
    }
}

package plic.lexer.token;

import data.product.Product;

public abstract class Token {
    public enum TokenType {
        IDENTIFIER, INTEGER, KEYWORD, SYMBOL
    }

    //protected Product<Long, Long> beginningPosition = new Product<>(0L, 0L);
    protected long lineNo;

    public Token(long line) {
        this.lineNo = line;
    }

    @Override
    public abstract String toString();

    public abstract TokenType getType();

    public long getLineNumber() {
        return this.lineNo;
    }
}

package plic.lexer.token;

public class SymbolToken extends Token {
    private String symbol;

    public SymbolToken(long line, String s) {
        super(line);
        this.symbol = s;
    }

    @Override
    public String toString() {
        return "symbol '" + symbol + "'";
    }

    @Override
    public TokenType getType() {
        return TokenType.SYMBOL;
    }

    public String getSymbol() {
        return symbol;
    }
}

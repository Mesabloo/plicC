package plic.lexer.token;

public class IdentifierToken extends Token {
    private String name;

    public IdentifierToken(long line, String n) {
        super(line);
        this.name = n;
    }

    @Override
    public String toString() {
        return "identifier '" + name + "'";
    }

    @Override
    public TokenType getType() {
        return TokenType.IDENTIFIER;
    }

    public String getName() {
        return this.name;
    }
}

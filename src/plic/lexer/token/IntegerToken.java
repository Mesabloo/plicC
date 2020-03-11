package plic.lexer.token;

/**
 * A token representing an integer.
 */
public class IntegerToken extends Token {
    private long nb;

    public IntegerToken(long line, long n) {
        super(line);
        this.nb = n;
    }

    @Override
    public String toString() {
        return "integer " + this.nb;
    }

    @Override
    public TokenType getType() {
        return TokenType.INTEGER;
    }

    public long getLong() {
        return this.nb;
    }
}

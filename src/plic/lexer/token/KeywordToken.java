package plic.lexer.token;

/**
 * A token representing a keyword.
 */
public class KeywordToken extends Token {
    private String kw;

    public KeywordToken(long line, String name) {
        super(line);
        this.kw = name;
    }

    @Override
    public String toString() {
        return "keyword '" + kw + "'";
    }

    @Override
    public TokenType getType() {
        return TokenType.KEYWORD;
    }

    public String getKeyword() {
        return kw;
    }
}

package plic.lexer.tokenizers;

import data.either.Either;
import data.product.Product;
import plic.lexer.Lexec;
import text.parser.combinators.Parseable;
import plic.lexer.combinators.String_;
import plic.lexer.stream.Reader;
import plic.lexer.token.KeywordToken;
import plic.lexer.token.Token;
import text.parser.combinators.error.ParseError;

import java.util.ArrayList;

public class KeywordLexer implements Lexec<Token> {
    public KeywordLexer() {

    }

    @Override
    public Product<Reader, Either<ParseError<Character, Reader>, Token>> apply(Reader reader) {
        return
            (String_.string(keywords.get(0))
                .orElse(String_.string(keywords.get(1)))
                .orElse(Parseable.empty())
            )
            .bind(kw -> Parseable.<Character, Reader>lineNumber()
                .fmap(line -> new KeywordToken(line, kw)))
            .fmap(t -> (Token) t)
            .parse(reader);
    }

    private static final ArrayList<String> keywords = new ArrayList<>();
    static {
        keywords.add("programme");
        keywords.add("entier");
    }
}
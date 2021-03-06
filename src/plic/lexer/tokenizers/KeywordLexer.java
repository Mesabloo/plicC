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

/**
 * Matches one of the keywords in the grammar.
 */
public class KeywordLexer implements Lexec<Token> {
    public KeywordLexer() {

    }

    @Override
    public Product<Reader, Either<ParseError<Character, Reader>, Token>> apply(Reader reader) {
        return
            (String_.string("programme").try_()
                .orElse(String_.string("ecrire").try_())
                .orElse(String_.string("entier").try_())
                .orElse(String_.string("et"))
                .orElse(String_.string("ou"))
                .orElse(String_.string("tableau").try_())
                .orElse(String_.string("tantque"))
                .orElse(String_.string("non"))
                .orElse(String_.string("sinon").try_())
                .orElse(String_.string("si"))
                .orElse(String_.string("alors"))
                .orElse(String_.string("lire"))
                .orElse(String_.string("pour"))
                .orElse(String_.string("dans"))
                .orElse(String_.string("repeter"))
                .orElse(Parseable.empty())
            )
            .then_(Parseable.<Character, Reader>satisfy(Character::isSpace).lookahead().void_())
            .try_()
            .bind(kw -> Parseable.<Character, Reader>lineNumber()
                .fmap(line -> new KeywordToken(line, kw)))
            .fmap(t -> (Token) t)
            .parse(reader);
    }
}

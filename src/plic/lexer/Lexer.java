package plic.lexer;

import data.either.Either;
import data.product.Product;
import org.jetbrains.annotations.NotNull;
import plic.lexer.exceptions.LexerException;
import plic.lexer.stream.Reader;
import plic.lexer.token.Token;
import plic.lexer.tokenizers.IdentifierLexer;
import plic.lexer.tokenizers.IntegerLexer;
import plic.lexer.tokenizers.KeywordLexer;
import plic.lexer.tokenizers.SymbolLexer;
import text.parser.combinators.Parseable;
import text.parser.combinators.error.ParseError;

import java.io.*;
import java.util.ArrayList;

import static text.parser.combinators.Parseable.pure;
import static plic.lexer.combinators.Lexeme.lexeme;

public class Lexer {
    private Reader stream;

    public Lexer(@NotNull String path) throws IOException {
        this.stream = new Reader(path);

        Product<Reader, Either<ParseError<Character, Reader>, Void>> res = lexeme(pure((Void) null)).parse(this.stream);
        this.stream = res.fst;
    }

    public Token next() {
        Product<Reader, Either<ParseError<Character, Reader>, Token>> res = lexeme(
                           new KeywordLexer().try_()
                   .orElse(new IdentifierLexer())
                   .orElse(new SymbolLexer())
                   .orElse(new IntegerLexer())
        ).parse(this.stream);

        this.stream = res.fst;
        if (res.snd.isLeft())
            throw new LexerException(res.snd.fromLeft());

        return res.snd.fromRight();
    }

    public boolean hasNext() {
        return !this.stream.eof();
    }

    public ArrayList<Token> lex() {
        ArrayList<Token> tks = new ArrayList<>();
        while (this.hasNext())
            tks.add(this.next());
        return tks;
    }
}

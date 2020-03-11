package plic.parser;

import data.either.Either;
import data.product.Product;
import plic.lexer.token.Token;
import plic.core.ProgramNode;
import plic.core.SyntaxTree;
import plic.parser.exceptions.ParserException;
import plic.parser.parsecs.ProgramParsec;
import plic.parser.stream.Reader;
import text.parser.combinators.error.ParseError;

import java.util.ArrayList;

/**
 * Basic parser class used to parse a given list of tokens into a {@link SyntaxTree}.
 */
public class Parser {
    private Reader tokens;

    public Parser(ArrayList<Token> tks) {
        this.tokens = new Reader(tks);
    }

    public SyntaxTree parse() {
        Product<Reader, Either<ParseError<Token, Reader>, ProgramNode>> res = new ProgramParsec().parse(tokens);

        if (res.snd.isLeft())
            throw new ParserException(res.snd.fromLeft());

        return new SyntaxTree(res.snd.fromRight());
    }
}

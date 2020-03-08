package plic.parser;

import data.either.Either;
import data.product.Product;
import plic.lexer.token.Token;
import plic.parser.ast.ProgramNode;
import plic.parser.ast.SyntaxTree;
import plic.parser.exceptions.ParserException;
import plic.parser.parsecs.ProgramParsec;
import plic.parser.stream.Reader;
import text.parser.combinators.error.ParseError;

import java.util.ArrayList;

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

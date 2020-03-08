package plic.parser.exceptions;

import plic.lexer.token.Token;
import plic.parser.stream.Reader;
import text.parser.combinators.error.ParseError;

public class ParserException extends RuntimeException {
    public ParserException(ParseError<Token, Reader> err) {
        super(err.toString());
    }
}

package plic.lexer.exceptions;

import plic.lexer.stream.Reader;
import text.parser.combinators.error.ParseError;

import java.util.stream.Stream;

public class LexerException extends RuntimeException {
    public LexerException(ParseError<Character, Reader> err) {
        super(err.toString());
    }
}

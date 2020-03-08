package plic.lexer;

import plic.lexer.stream.Reader;
import text.parser.combinators.Parseable;

public interface Lexec<A> extends Parseable<Character, Reader, A> {
}

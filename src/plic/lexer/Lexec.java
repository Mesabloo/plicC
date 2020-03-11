package plic.lexer;

import plic.lexer.stream.Reader;
import text.parser.combinators.Parseable;

/**
 * A simple type alias for a {@link Parseable} on a {@link Reader}.
 * @param <A>
 */
public interface Lexec<A> extends Parseable<Character, Reader, A> {
}

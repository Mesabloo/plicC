package plic.parser;

import plic.lexer.token.Token;
import plic.parser.stream.Reader;
import text.parser.combinators.Parseable;

/**
 * A simple alias for a {@link Parseable}.
 * @param <A>
 */
public interface Parsec<A> extends Parseable<Token, Reader, A> {
}

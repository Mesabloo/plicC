package text.parser.combinators.error;

/**
 * A basic superclass for {@link ParseError} items.
 * @param <Token> A token of the stream.
 */
public abstract class ErrorItem<Token> implements Comparable<ErrorItem<Token>> {
}

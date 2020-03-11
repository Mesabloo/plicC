package text.parser.combinators;

/**
 * A basic stream interface supporting lookahead and backtracking.
 * @param <S> The type to be stored in a stream (for example {@link Character})
 */
public interface Stream<S> {
    /**
     * Gets an element of the stream but without touching to the pointer.
     * It calls {@link Stream#next()} followed by {@link Stream#back()} to backtrack.
     * @return The element fetched from the stream
     */
    default S lookahead() {
        final S elem = next();
        back();
        return elem;
    }

    /**
     * Gets the next element and move the pointer forward.
     * @return The element fetched
     */
    S next();

    /**
     * Backtracks in the current stream (move the pointer backwards).
     * @return The previous element in the stream
     */
    S back();
    boolean eof();

    /**
     * @return The current offset in the stream
     */
    long getOffset();

    /**
     * (Assuming streams do have line numbers)
     * @return The current line number
     */
    long getLineNumber();
}

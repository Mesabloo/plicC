package text.parser.combinators.error;

import text.parser.combinators.Stream;
import text.parser.combinators.error.item.EOF;
import text.parser.combinators.error.item.Label;
import text.parser.combinators.error.item.Tokens;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * A parse error wrapper
 * @param <Token>
 * @param <S>
 */
public class ParseError<Token, S extends Stream<Token>> {
    protected long offset;
    protected Optional<ErrorItem<Token>> unexpected;
    protected Set<ErrorItem<Token>> expected;

    private ParseError(long off, Optional<ErrorItem<Token>> unexpected, Set<ErrorItem<Token>> expected) {
        this.offset = off;
        this.unexpected = unexpected;
        this.expected = expected;
    }

    /**
     * Construct an "unexpected EOF" parse error.
     * @param stream
     * @param <Token>
     * @param <S>
     * @return
     */
    public static <Token, S extends Stream<Token>> ParseError<Token, S> eof(S stream) {
        return new ParseError<>(stream.getLineNumber(), Optional.of(new EOF<>()), new HashSet<>());
    }

    /**
     * Constructs a "Unexpected &lt;label&gt;" parse error.
     * @param stream
     * @param label
     * @param <Token>
     * @param <S>
     * @return
     */
    public static <Token, S extends Stream<Token>> ParseError<Token, S> label(S stream, String label) {
        return new ParseError<>(stream.getLineNumber(), Optional.of(new Label<>(label)), new HashSet<>());
    }

    /**
     * Constructs an "Unexpected &lt;token1&gt;, &lt;token2&gt;...&lt;tokenN&gt;" parse error.
     * @param stream
     * @param tks
     * @param <Token>
     * @param <S>
     * @return
     */
    public static <Token, S extends Stream<Token>> ParseError<Token, S> tokens(S stream, ArrayList<Token> tks) {
        return new ParseError<>(stream.getLineNumber(), Optional.of(new Tokens<>(tks)), new HashSet<>());
    }

    /**
     * Constructs an empty parse error
     * @param stream
     * @param <Token>
     * @param <S>
     * @return
     */
    public static <Token, S extends Stream<Token>> ParseError<Token, S> empty(S stream) {
        return new ParseError<>(stream.getLineNumber(), Optional.empty(), new HashSet<>());
    }

    /**
     * Appends two parse errors together, keeping the longest match.
     * @param err
     * @return
     */
    public ParseError<Token, S> mappend(ParseError<Token, S> err) {
        if (this.offset < err.offset)
            return err;
        if (this.offset > err.offset)
            return this;

        BiFunction<Optional<ErrorItem<Token>>, Optional<ErrorItem<Token>>, Optional<ErrorItem<Token>>> f =
            (o1, o2) -> {
                if (!o1.isPresent() && !o2.isPresent())
                    return Optional.empty();
                if (!o1.isPresent())
                    return o2;
                if (!o2.isPresent())
                    return o1;

                ErrorItem<Token> e1 = o1.get();
                ErrorItem<Token> e2 = o2.get();
                return Optional.of(e1.compareTo(e2) >= 0 ? e1 : e2);
            };

        this.expected.addAll(err.expected);
        return new ParseError<Token, S>(this.offset, f.apply(this.unexpected, err.unexpected), this.expected);
    }

    @Override
    public String toString() {
        final StringBuilder bd = new StringBuilder();
        bd.append(String.format("At line %d", this.offset));
        this.unexpected.ifPresent(tk -> { bd.append(", unexpected "); bd.append(tk.toString()); });
        if (!this.expected.isEmpty()) {
            bd.append(", expected one of ");
            bd.append(this.expected.stream().map(Object::toString).collect(Collectors.joining(", ")));
        }
        return bd.toString();
    }
}

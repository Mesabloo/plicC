package text.parser.combinators.error.item;

import org.jetbrains.annotations.NotNull;
import text.parser.combinators.error.ErrorItem;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * A token parse error item.
 * @param <T>
 */
public class Tokens<T> extends ErrorItem<T> {
    @Override
    public int compareTo(@NotNull ErrorItem<T> tErrorItem) {
        if (tErrorItem instanceof Tokens)
            return 0;
        return -1;
    }

    private ArrayList<T> tokens;

    public Tokens(ArrayList<T> tks) {
        this.tokens = tks;
    }

    @Override
    public String toString() {
        return this.tokens.stream().map(Object::toString).collect(Collectors.joining(", "));
    }
}

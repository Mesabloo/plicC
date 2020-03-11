package text.parser.combinators.error.item;

import org.jetbrains.annotations.NotNull;
import text.parser.combinators.error.ErrorItem;

/**
 * The EOF parse error item.
 * @param <T>
 */
public class EOF<T> extends ErrorItem<T> {
    @Override
    public int compareTo(@NotNull ErrorItem<T> tErrorItem) {
        if (tErrorItem instanceof EOF)
            return 0;
        return 1;
    }

    @Override
    public String toString() {
        return "<EOF>";
    }
}

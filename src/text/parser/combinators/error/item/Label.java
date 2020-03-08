package text.parser.combinators.error.item;

import org.jetbrains.annotations.NotNull;
import text.parser.combinators.error.ErrorItem;

public class Label<T> extends ErrorItem<T> {
    @Override
    public int compareTo(@NotNull ErrorItem<T> tErrorItem) {
        if (tErrorItem instanceof EOF)
            return -1;
        if (tErrorItem instanceof Tokens)
            return 1;
        return 0;
    }

    private String lbl;

    public Label(String label) {
        this.lbl = label;
    }

    @Override
    public String toString() {
        return this.lbl;
    }
}

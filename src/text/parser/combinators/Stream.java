package text.parser.combinators;

import control.copy.Copiable;

public interface Stream<S> {
    default S lookahead() {
        final S elem = next();
        back();
        return elem;
    }

    S next();
    S back();
    boolean eof();

    long getOffset();
    long getLineNumber();
}

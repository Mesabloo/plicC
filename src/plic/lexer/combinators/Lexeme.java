package plic.lexer.combinators;

import text.parser.combinators.Parseable;
import plic.lexer.stream.Reader;

import java.util.ArrayList;
import java.util.function.Predicate;

public class Lexeme {
    public static <A> Parseable<Character, Reader, A> lexeme(Parseable<Character, Reader, A> p) {
        Parseable<Character, Reader, Void> skipped =
            Parseable.<Character, Reader>satisfy(isSpaceOrNewline()).some().void_()
            .orElse(lineComment().try_());
        return p.then_(skipped.many()
            .orElse(Parseable.<Character, Reader>eof().bind(e -> Parseable.pure(new ArrayList<>())))
        );
    }

    private static Predicate<Character> isSpaceOrNewline() {
        return c -> c == ' ' || c == '\t' || c == '\n' || c == '\r';
    }

    private static Parseable<Character, Reader, Void> lineComment() {
        return String_.string("//").then(Parseable.<Character, Reader>takeWhile(isNotNewline())).void_();
    }

    private static Predicate<Character> isNotNewline() {
        return c -> c != '\n';
    }
}

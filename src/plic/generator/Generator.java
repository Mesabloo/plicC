package plic.generator;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface Generator {
    StringBuilder generate(StringBuilder builder, int n);

    default String genIndent(int indent) {
        return Stream.generate(() -> " ").limit(indent).collect(Collectors.joining(""));
    }
}

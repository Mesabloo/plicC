package plic.core;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class TreeNode {
    @Override
    public String toString() {
        return this.toString_(0);
    }

    public String toString_(int level) {
        return Stream.generate(() -> " ").limit(level).collect(Collectors.joining());
    }

    protected static final int INDENT = 4;
}

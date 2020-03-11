package plic.lexer.stream;

import control.copy.Copiable;
import org.jetbrains.annotations.NotNull;
import text.parser.combinators.Stream;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * The {@link Reader} class is a wrapper around a {@link Stream} of characters.
 */
public final class Reader implements Copiable<Reader>, Stream<Character> {
    // Underlying input stream from which we fetch characters.
    private ArrayList<Character> input;
    private int pointer;

    private int lineNumber;

    public final static char EOF = (char) -1;

    private Reader() {}

    public Reader(@NotNull String path) throws IOException {
        this.pointer = 0;
        this.lineNumber = 1;
        this.input = new ArrayList<>();

        try (RandomAccessFile raf = new RandomAccessFile(path, "r")) {
            byte[] bytes = new byte[(int) raf.length()];
            raf.readFully(bytes);
            for (byte b : bytes)
                this.input.add((char) b);
            this.input.add(' ');
        }
    }

    @Override
    public Character next() {
        char c = this.input.get(this.pointer++);
        if (c == '\n')
            lineNumber++;
        return this.pointer < this.input.size() ? c : EOF;
    }

    @Override
    public Character back() {
        --this.pointer;
        char c = this.input.get(this.pointer);
        if (c == '\n')
            lineNumber--;
        return this.pointer >= 0 ? c : EOF;
    }

    @Override
    public boolean eof() {
        return lookahead() == EOF;
    }

    @Override
    public long getOffset() {
        return pointer;
    }

    @Override
    public long getLineNumber() {
        return this.lineNumber;
    }

    @Override
    public Reader copy() {
        Reader r = new Reader();
        r.input = (ArrayList<Character>) this.input.clone();
        r.pointer = this.pointer;
        r.lineNumber = this.lineNumber;
        return r;
    }
}

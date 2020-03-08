package plic.parser.stream;

import control.copy.Copiable;
import plic.lexer.token.Token;
import text.parser.combinators.Stream;

import java.util.ArrayList;

public class Reader implements Stream<Token>, Copiable<Reader> {
    private ArrayList<Token> tokens;
    private int pointer;

    private Reader() {

    }

    public Reader(ArrayList<Token> tks) {
        this.tokens = tks;
        this.pointer = 0;
    }

    @Override
    public Token next() {
        Token c = this.tokens.get(this.pointer++);
        return this.pointer <= this.tokens.size() ? c : null;
    }

    @Override
    public Token back() {
        --this.pointer;
        return this.eof() ? this.tokens.get(this.pointer) : null;
    }

    @Override
    public boolean eof() {
        return pointer >= tokens.size() || pointer < 0;
    }

    @Override
    public long getOffset() {
        return pointer;
    }

    @Override
    public long getLineNumber() {
        if (pointer <= 0)
            return 1;
        if (pointer >= tokens.size())
            return tokens.get(tokens.size() - 1).getLineNumber();
        return tokens.get(pointer).getLineNumber();
    }

    @Override
    public Reader copy() {
        Reader r = new Reader();
        r.tokens = (ArrayList<Token>) this.tokens.clone();
        r.pointer = this.pointer;
        return r;
    }
}

package com.github.saka1029.obscure.command;

import java.io.IOException;
import java.io.Reader;
import java.util.function.IntConsumer;

public class PeekableReader extends Reader {

    private final Reader in;
    private final IntConsumer peeker;
    
    public PeekableReader(Reader in, IntConsumer peeker) {
        this.in = in;
        this.peeker = peeker;
    }

    private boolean eof = false;
    
    @Override
    public int read() throws IOException {
        int c = in.read();
        if (c == -1)
            eof = true;
        peeker.accept(c);
        return c;
    }
    
    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        if (eof) return -1;
        int size = 0;
        for (int i = off, max = off + len; i < max; ++i, ++size) {
            int ch = read();
            if (ch == -1)
                break;
            cbuf[i] = (char)ch;
        }
        return size;
    }

    @Override
    public void close() throws IOException {
        in.close();
    }

}

package io.github.saka1029.script;

import java.io.IOException;
import java.io.Writer;
import java.util.function.IntConsumer;

public class PeekableWriter extends Writer {

    private final Writer out;
    private final IntConsumer peeker;
    
    public PeekableWriter(Writer out, IntConsumer peeker) {
        this.out = out;
        this.peeker = peeker;
    }

    @Override
    public void write(int c) throws IOException {
        peeker.accept(c);
        out.write(c);
    }
    
    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        for (int i = off, max = off + len; i < max; ++i)
            write(cbuf[i]);
    }

    @Override
    public void flush() throws IOException {
        out.flush();
    }

    @Override
    public void close() throws IOException {
        out.close();
    }

}

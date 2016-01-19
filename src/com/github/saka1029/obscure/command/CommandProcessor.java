package com.github.saka1029.obscure.command;

import java.io.Reader;
import java.io.Writer;

public interface CommandProcessor {

    void run(Reader reader, Writer writer, Writer error);
    
}

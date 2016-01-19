package com.github.saka1029.obscure.core;

import java.io.IOException;
import java.io.Writer;

import com.github.saka1029.obscure.command.CommandProcessor;
import static com.github.saka1029.obscure.core.Global.*;

public class ObscureCommand implements CommandProcessor {

    @Override
    public void run(java.io.Reader reader, Writer writer, Writer error) {
        try {
            Reader in = new Reader(reader);
            Env env = Env.create();
            while (true) {
                try {
                    Object o = in.read();
                    Object x = eval(o, env);
                    if (x == Reader.EOF_OBJECT)
                        break;
                    writer.write(x + "\n");
                    writer.flush();
                } catch (Exception e) {
                    error.write(e.getMessage() + "\n");
                    error.flush();
                }
            }
        } catch (IOException e) {
            System.err.println(e);
        }
        
    }

}

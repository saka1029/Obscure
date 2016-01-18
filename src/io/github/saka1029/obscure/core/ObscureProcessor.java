package io.github.saka1029.obscure.core;

import java.io.IOException;
import java.io.Writer;

import io.github.saka1029.script.CommandProcessor;
import static io.github.saka1029.obscure.core.Obscure.*;

public class ObscureProcessor implements CommandProcessor {

    @Override
    public void run(java.io.Reader reader, Writer writer, Writer error) {
        try {
            Reader in = new Reader(reader);
            Env env = env();
            while (true) {
                try {
                    Object o = in.read();
                    Object x = eval(o, env);
                    if (x == Reader.EOF_OBJECT)
                        break;
                    writer.write(x + "\n");
                    writer.flush();
                } catch (Exception e) {
                    error.write(e + "\n");
                    error.flush();
                }
            }
        } catch (IOException e) {
            System.err.println(e);
        }
        
    }

}

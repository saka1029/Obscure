package com.github.saka1029.obscure.core;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;

import com.github.saka1029.obscure.command.CommandProcessor;
import static com.github.saka1029.obscure.core.Global.*;

public class ObscureCommand implements CommandProcessor {

    static Throwable cause(Throwable t) {
        Throwable prev = t;
        while (true) {
            if (t instanceof InvocationTargetException)
                t = ((InvocationTargetException) t).getTargetException();
            else if (t.getCause() != null)
                t = t.getCause();
            if (t == prev) break;
            prev = t;
        }
        return t;
    }

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
                    writer.write(print(x) + "\n");
                    writer.flush();
                } catch (Exception e) {
//                    e.printStackTrace();
                    if (e instanceof ObscureException)
                    error.write(cause(e).getMessage() + "\n");
                    error.flush();
                    INDENT = 0;
                }
            }
        } catch (IOException e) {
            System.err.println(e);
        }
        
    }

}

package io.github.saka1029.script;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;

public class CommandRunner {
    
    final CommandProcessor processor;
    final Reader reader;
    final Writer writer;
    final Writer error;
    String prompt = "> ";
    
    boolean readNewline = false;
    boolean writeNewline = false;
    
    void prompt() {
        try {
            writer.write(prompt);
            writer.flush();
            writeNewline = false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void peekRead(int ch) {
        if (ch == '\n')
            if (writeNewline)
                prompt();
        readNewline = false;
    }

    void peekWrite(int ch) {
        if (ch == '\n')
            writeNewline = true;
    }

    public CommandRunner(CommandProcessor processor) {
        this.processor = processor;
        this.reader = new PeekableReader(new InputStreamReader(System.in, Charset.defaultCharset()), this::peekRead);
        this.writer = new PeekableWriter(new OutputStreamWriter(System.out, Charset.defaultCharset()), this::peekWrite);
        this.error = new PeekableWriter(new OutputStreamWriter(System.err, Charset.defaultCharset()), this::peekWrite);
    }

    public void run() throws IOException {
        prompt();
        processor.run(reader, writer, error);
    }
    
    public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException, IOException {
        String p = null;
        for (int i = 0, l = args.length; i < l; ++i)
            switch (args[i]) {
                case "-l":
                    if (i >= l)
                        throw new IllegalArgumentException("no arguments for -l");
                    p = args[++i];
                    break;
                default:
                    throw new IllegalArgumentException("unknown option: " + args[i]);
            }
        CommandProcessor cp = (CommandProcessor)Class.forName(p).newInstance();
        CommandRunner runner = new CommandRunner(cp);
        runner.run();
    }

}

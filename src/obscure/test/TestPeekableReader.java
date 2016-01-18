package obscure.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.util.function.IntConsumer;

import org.junit.Test;

import io.github.saka1029.script.PeekableReader;
import io.github.saka1029.script.PeekableWriter;

public class TestPeekableReader {

    IntConsumer observer = ch -> System.out.println("accept: " + ((char)ch));

    @Test
    public void testObservableReader() throws IOException {
        String s = "abc";
        try (Reader r = new PeekableReader(new StringReader(s), observer)) {
            while (true) {
                int c = r.read();
                if (c == -1) break;
            }
            
        }
    }

    @Test
    public void testObservableReaderBuffer() throws IOException {
        String s = "abc";
        try (Reader r = new PeekableReader(new StringReader(s), observer)) {
            char[] buffer = new char[100];
            int size;
            while ((size = r.read(buffer)) != -1) {
                System.out.println(size);
            }
            
        }
    }
    
    static IntConsumer peeker = c -> { System.out.println("peek:" + c); };
    
    @Test
    public void testReadWrite() throws IOException {
        try (Reader reader = new PeekableReader(new InputStreamReader(System.in), peeker);
             Writer writer = new PeekableWriter(new OutputStreamWriter(System.out), peeker)) {

            
        }
    }

}

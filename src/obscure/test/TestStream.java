package obscure.test;

import java.io.IOException;
import java.util.function.Function;

import org.junit.Test;

import obscure.core.Env;
import static obscure.core.ListHelper.*;
import static org.junit.Assert.*;

import obscure.core.Reader;

public class TestStream {

    static Object read(String s) throws IOException {
        return new Reader(s).read();
    }

    static Object evalRead(String s, Env env) throws IOException {
        Object input = read(s);
        Object output = eval(input, env);
        System.out.printf("%s -> %s%n", print(input), print(output));
        return output;
    }

    public static Object call(Function<Object, Object> f) {
        return f.apply(10);
    }

    @Test
    public void testJavaFunction() throws IOException {
        Env env = Env.create();
        evalRead("(define f (lambda (x) (* x x)))", env);
        assertEquals(9, evalRead("(f 3)", env));
        evalRead("(define TestClass (Class forName \"" + getClass().getName() + "\"))", env);
        assertEquals(100, evalRead("(TestClass call f)", env));
    }
    
    @Test
    public void testStream() throws IOException {
        Env env = Env.create();
        evalRead("(define Stream (Class forName \"java.util.stream.Stream\"))", env);
        assertArrayEquals(new Object[] {1, 4, 9},
            (Object[])evalRead("(; (Stream of 1 2 3) (map (lambda (x) (* x x))) (toArray))", env));
    }
    
    @Test
    public void testIntStream() throws IOException {
        Env env = Env.create();
        evalRead("(define IntStream (Class forName \"java.util.stream.IntStream\"))", env);
        assertEquals(3, evalRead("(define c 3)", env));
        assertArrayEquals(new int[] {4, 7, 12},
            (int[])evalRead(
                    "(; (IntStream of 1 2 3)"
                    + "(map (lambda (x) (+ c (* x x))))"
                    + "(toArray))", env));
    }
    
    @Test
    public void testLongStream() throws IOException {
        Env env = Env.create();
        evalRead("(define LongStream (Class forName \"java.util.stream.LongStream\"))", env);
        assertEquals(3L, evalRead("(define c 3L)", env));
        assertArrayEquals(new long[] {4, 7, 12},
            (long[])evalRead(
                    "(; (LongStream of 1L 2L 3L)"
                    + "(map (lambda (x) (+ c (* x x))))"
                    + "(toArray))", env));
    }

}

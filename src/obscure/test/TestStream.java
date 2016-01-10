package obscure.test;

import java.io.IOException;
import java.util.function.Function;

import org.junit.Test;

import obscure.core.Env;
import static obscure.core.Helper.*;
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
            (Object[])evalRead("(((Stream of 1 2 3) map (lambda (x) (* x x))) toArray)", env));
        assertArrayEquals(new Object[] {1, 4, 9},
            (Object[])evalRead("(; (Stream of 1 2 3) (map (lambda (x) (* x x))) (toArray))", env));
    }
    
    @Test
    public void testIntStream() throws IOException {
        Env env = Env.create();
        evalRead("(define IntStream (Class forName \"java.util.stream.IntStream\"))", env);
        assertEquals(3, evalRead("(define c 3)", env));
        assertArrayEquals(new int[] {4, 7, 12}, (int[])evalRead(
                    "(; (IntStream of 1 2 3)"
                    + "(map (lambda (x) (+ c (* x x))))"
                    + "(toArray))", env));
    }
    
    @Test
    public void testIntStreamIntBinaryOperator() throws IOException {
        Env env = Env.create();
        evalRead("(define IntStream (Class forName \"java.util.stream.IntStream\"))", env);
        assertEquals(24, evalRead(
                    "(; (IntStream of 1 2 3 4)"
                    + "(reduce (lambda (x y) (* x y)))"
                    + "(getAsInt))", env));
    }
    
    @Test
    public void testLongStream() throws IOException {
        Env env = Env.create();
        evalRead("(define LongStream (Class forName \"java.util.stream.LongStream\"))", env);
        assertEquals(3L, evalRead("(define c 3L)", env));
        assertArrayEquals(new long[] {4, 7, 12}, (long[])evalRead(
                    "(; (LongStream of 1L 2L 3L)"
                    + "(map (lambda (x) (+ c (* x x))))"
                    + "(toArray))", env));
    }
    
    @Test
    public void testStreamBiFunction() throws IOException {
        Env env = Env.create();
        evalRead("(define Stream (Class forName \"java.util.stream.Stream\"))", env);
        assertEquals("abc", evalRead(
                    "(; (Stream of \"a\" \"b\" \"c\")"
                    + "(reduce (lambda (x y) (+ x y)))"
                    + "(get))", env));
    }

    @Test
    public void testSupplier() throws IOException {
        Env env = Env.create();
        evalRead("(define Stream (Class forName \"java.util.stream.Stream\"))", env);
        assertEquals(100, evalRead("(define c 100)", env));
        assertArrayEquals(new Object[] {1, 2, 3, 4}, (Object[])evalRead(
            "(let ((c 0))"
                + "(; (Stream generate (lambda () (set c (+ c 1))))"
                + "(limit 4L)"
                + "(toArray)))", env));
        assertEquals(100, evalRead("c", env));
    }

    static String stringX(String s) {
        return s.replaceAll("(?!^)x(?!$)", "");
    }

    static String stringX2(String s) {
        int l = s.length();
        return l <= 2 ? s : s.substring(0, 1) + s.substring(1, l - 1).replaceAll("x", "")+ s.substring(l - 1);
    }

    @Test
    public void testLengthOfLongestSubstring() {
        System.out.println(stringX("xxHxix") + ":" + stringX2("xxHxix"));
        System.out.println(stringX("abxxxcd") + ":" + stringX2("abxxxcd"));
        System.out.println(stringX("xabxxxcdx") + ":" + stringX2("xabxxxcdx"));
    }
}

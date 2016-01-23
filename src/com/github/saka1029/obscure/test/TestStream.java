package com.github.saka1029.obscure.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.github.saka1029.obscure.core.Env;
import static com.github.saka1029.obscure.core.Global.*;

public class TestStream {

    static Env env = Env.create();
    
    @BeforeClass
    public static void before() throws IOException {
        defineGlobal("IntStream", IntStream.class);
        defineGlobal("Array", Array.class);
        defineGlobal("String", String.class);
        defineGlobal("Stream", Stream.class);
    }

    @Test
    public void test() throws IOException {
        assertArrayEquals(new int[]{1, 2, 3}, (int[])eval(read(
            "(IntStream"
            + " (of 1 2 3)"
            + " (toArray))"), env));
        assertArrayEquals(new int[]{2, 3, 4}, (int[])eval(read(
            "(IntStream"
            + " (of 1 2 3)"
            + " (map (lambda (x) (+ x 1)))"
            + " (toArray))"), env));
    }

    @Test
    public void testFilter() throws IOException {
        assertArrayEquals(new int[]{1, 2}, (int[])eval(read(
            "(IntStream"
            + " (of 1 2 3)"
            + " (filter (lambda (x) (< x 3)))"
            + " (toArray))"), env));
    }

    @Test
    public void testMapToObj() throws IOException {
        assertArrayEquals(new Object[]{"a1", "a2", "a3"}, (Object[])eval(read(
            "(IntStream"
            + " (of 1 2 3)"
            + " (mapToObj (lambda (x) (+ \"a\" x)))"
            + " (toArray))"), env));
    }

    @Test
    public void testMapToObjString() throws IOException {
        assertArrayEquals(new String[]{"a1", "a2", "a3"}, (String[])eval(read(
            "(IntStream"
            + " (of 1 2 3)"
            + " (mapToObj (lambda (x) (+ \"a\" x)))"
            + " (toArray (lambda (n) (Array (newInstance String n)))))"), env));
    }

    /**
     * (lambda (x) (< x 3)) は　Applicable なのでオブジェクトとして扱うことができない。
     * そのため (negate) は Closure に対する引数とみなされる。
     */
    @Test
    @Ignore
    public void testFilterNigate() throws IOException {
        assertArrayEquals(new int[]{1, 2}, (int[])eval(read(
            "(IntStream"
            + " (of 1 2 3)"
            + " (filter ((lambda (x) (< x 3)) (negate)))"
            + " (toArray))"), env));
    }

}

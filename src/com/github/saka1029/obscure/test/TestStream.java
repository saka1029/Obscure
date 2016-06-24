package com.github.saka1029.obscure.test;

import static org.junit.Assert.*;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.stream.Collectors;
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
    public static void before() {
        defineGlobalEnv("IntStream", IntStream.class);
        defineGlobalEnv("Array", Array.class);
        defineGlobalEnv("String", String.class);
        defineGlobalEnv("Stream", Stream.class);
        defineGlobalEnv("Collectors", Collectors.class);
    }

    @Test
    public void test() {
        assertArrayEquals(new int[]{1, 2, 3}, (int[])eval(read(
            "(IntStream"
            + " (of 1 2 3)"
            + " (toArray))"), env));
    }
    
    @Test
    public void testMap() {
        assertArrayEquals(new int[]{2, 3, 4}, (int[])eval(read(
            "(IntStream"
            + " (of 1 2 3)"
            + " (map (lambda (x) (+ x 1)))"
            + " (toArray))"), env));
    }

    @Test
    public void testFilter() {
        assertArrayEquals(new int[]{1, 2}, (int[])eval(read(
            "(IntStream"
            + " (of 1 2 3)"
            + " (filter (lambda (x) (< x 3)))"
            + " (toArray))"), env));
    }

    @Test
    public void testMapToObj() {
        assertArrayEquals(new Object[]{"a1", "a2", "a3"}, (Object[])eval(read(
            "(IntStream"
            + " (of 1 2 3)"
            + " (mapToObj (lambda (x) (+ \"a\" x)))"
            + " (toArray))"), env));
    }

    @Test
    public void testMapToObjToList() {
        assertEquals(Arrays.asList("a1", "a2", "a3"), (java.util.List<?>)eval(read(
            "(IntStream"
            + " (of 1 2 3)"
            + " (mapToObj (lambda (x) (+ \"a\" x)))"
            + " (collect (Collectors (toList))))"), env));
    }

    @Test
    public void testMapToObjString() {
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
    public void testFilterNigate() {
        assertArrayEquals(new int[]{1, 2}, (int[])eval(read(
            "(IntStream"
            + " (of 1 2 3)"
            + " (filter ((lambda (x) (< x 3)) (negate)))"
            + " (toArray))"), env));
    }

    @Test
    public void testReduce() {
        assertEquals(6, eval(read(
            "(IntStream"
            + " (range 1 4)"
//            + " (of 1 2 3)"
            + " (reduce 0 (lambda (x y) (+ x y)))  )"), env));
    }

}

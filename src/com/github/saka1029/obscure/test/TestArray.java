package com.github.saka1029.obscure.test;

import static org.junit.Assert.*;


import java.io.IOException;
import java.lang.reflect.Array;
import java.util.function.Consumer;

import org.junit.BeforeClass;
import org.junit.Test;

import com.github.saka1029.obscure.core.Env;
import com.github.saka1029.obscure.core.List;
import com.github.saka1029.obscure.core.Procedure;

import static com.github.saka1029.obscure.core.Global.*;

/**
 * 配列の作成はグローバルプロシジャ array で実行する。
 * 配列のアクセスはObject[].classのメソッド length, get, setで実行する。
 *
 */
public class TestArray {

    static Env env = Env.create();
   
    static int[] toIntArray(Object obj) {
        List dims = (List)obj;
        int size = dims.size();
        int[] result = new int[size];
        for (int i = 0; i < size; ++i, dims = (List)cdr(dims))
            result[i] = (int)car(dims);
        return result;
    }

    static Object first(Object f, Consumer<Object> consumer) {
        consumer.accept(f);
        return f;
    }

    @BeforeClass
    public static void before() throws IOException {
        defineGlobal("Array", Array.class);
        defineGlobal("int", Integer.TYPE);
        defineGlobal("Integer", Integer.class);
        defineGlobal("array", (Procedure)(self, args) -> Array.newInstance((Class<?>)car(args), toIntArray(cdr(args))));
        defineGlobal("set", (Procedure)(self, args) -> {
            Object value = caddr(args);
            Array.set(car(args), (int)cadr(args), value);
            return value;
        });
        defineGlobal("get", (Procedure)(self, args) -> Array.get(car(args), (int)cadr(args)));
        defineClass(Object[].class, "get", (Procedure)(self, args) -> Array.get(self, (int)car(args)));
        defineClass(Object[].class, "set", (Procedure)(self, args) -> first(cadr(args), x -> Array.set(self, (int)car(args), x)));
        defineClass(Object[].class, "length", (Procedure)(self, args) -> Array.getLength(self));
    }

    @Test
    public void testIntArray() throws IOException {
        assertArrayEquals(new int[] {0, 0}, (int[])eval(read("(define a (Array (newInstance int 2)))"), env));
        eval(read("(Array (set a 0 100))"), env);
        eval(read("(Array (set a 1 200))"), env);
        assertArrayEquals(new int[] {100, 200}, (int[])eval(read("a"), env));
    }

    @Test
    public void testIntArrayProcedure() throws IOException {
        assertArrayEquals(new int[] {0, 0}, (int[])eval(read("(define a (array int 2))"), env));
        assertEquals(100, eval(read("(set a 0 100)"), env));
        assertEquals(200, eval(read("(set a 1 200)"), env));
        assertArrayEquals(new int[] {100, 200}, (int[])eval(read("a"), env));
        assertEquals(100, eval(read("(get a 0)"), env));
        assertEquals(200, eval(read("(get a 1)"), env));
    }

    @Test
    public void testIntegerArrayProcedure() throws IOException {
        assertArrayEquals(new Integer[] {null, null}, (Integer[])eval(read("(define a (array Integer 2))"), env));
        assertEquals(100, eval(read("(set a 0 100)"), env));
        assertEquals(200, eval(read("(set a 1 200)"), env));
        assertArrayEquals(new Integer[] {100, 200}, (Integer[])eval(read("a"), env));
        assertEquals(100, eval(read("(get a 0)"), env));
        assertEquals(200, eval(read("(get a 1)"), env));
    }

    @Test
    public void testIntArray2d() throws IOException {
        assertArrayEquals(new int[][] {{0, 0},{0, 0}}, (int[][])eval(read("(define a (array int 2 2))"), env));
        assertEquals(100, eval(read("(set (get a 0) 0 100)"), env));
        assertEquals(200, eval(read("(set (get a 0) 1 200)"), env));
        assertArrayEquals(new int[][] {{100, 200}, {0, 0}}, (int[][])eval(read("a"), env));
        assertEquals(100, eval(read("(get (get a 0) 0)"), env));
        assertEquals(200, eval(read("(get (get a 0) 1)"), env));
    }
    
    @Test
    public void testMethod() throws IOException {
        assertArrayEquals(new int[] {0, 0}, (int[])eval(read("(define a (array int 2))"), env));
        assertEquals(100, eval(read("(a (set 0 100))"), env));
        assertEquals(200, eval(read("(a (set 1 200))"), env));
        assertArrayEquals(new int[] {100, 200}, (int[])eval(read("a"), env));
        assertEquals(100, eval(read("(a (get 0))"), env));
        assertEquals(201, eval(read("(a (get 1) (+ 1))"), env));
    }
    
    @Test
    public void testMethod2d() throws IOException {
        assertArrayEquals(new int[][] {{0, 0}, {0, 0}}, (int[][])eval(read("(define a (array int 2 2))"), env));
        assertEquals(2, eval(read("(a (length))"), env));
        assertEquals(2, eval(read("(a (get 0) (length))"), env));
        assertEquals(100, eval(read("(a (get 0) (set 0 100))"), env));
        assertEquals(200, eval(read("(a (get 0) (set 1 200))"), env));
        assertArrayEquals(new int[][] {{100, 200}, {0, 0}}, (int[][])eval(read("a"), env));
        assertEquals(100, eval(read("(a (get 0) (get 0))"), env));
        assertEquals(201, eval(read("(a (get 0) (get 1) (+ 1))"), env));
    }

}

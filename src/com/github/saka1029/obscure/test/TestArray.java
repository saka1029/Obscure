package com.github.saka1029.obscure.test;

import static org.junit.Assert.*;


import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Array;

import org.junit.BeforeClass;
import org.junit.Test;

import com.github.saka1029.obscure.core.Env;
import com.github.saka1029.obscure.core.Procedure;

import static com.github.saka1029.obscure.core.Global.*;

/**
 * 配列の作成はグローバルプロシジャ array で実行する。
 * 配列のアクセスはObject[].classのメソッド length, get, setで実行する。
 *
 */
public class TestArray {

    static Env env = Env.create();
   
    @BeforeClass
    public static void before() throws IOException {
        defineGlobalEnv("Array", Array.class);
        defineGlobalEnv("set", (Procedure)(self, args) -> {
            Object value = caddr(args);
            Array.set(car(args), (int)cadr(args), value);
            return value;
        });
        defineGlobalEnv("get", (Procedure)(self, args) -> Array.get(car(args), (int)cadr(args)));
    }

    @Test
    public void testArraySuper() {
        assertEquals(Object.class, int[].class.getSuperclass());
        assertArrayEquals(new Class[]{ Cloneable.class, Serializable.class}, int[].class.getInterfaces());
        assertArrayEquals(new Class[]{ Cloneable.class, Serializable.class}, Integer[].class.getInterfaces());
        assertFalse(Object[].class.isAssignableFrom(int[].class));
        assertTrue(Object[].class.isAssignableFrom(Integer[].class));
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

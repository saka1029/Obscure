package com.github.saka1029.obscure.test;

import static org.junit.Assert.*;


import java.io.Serializable;

import org.junit.BeforeClass;
import org.junit.Test;

import com.github.saka1029.obscure.core.Env;

import static com.github.saka1029.obscure.core.Global.*;

/**
 * 配列の作成はグローバルプロシジャ array で実行する。
 * 配列のアクセスはObject[].classのメソッド length, get, setで実行する。
 *
 */
public class TestArray {

    static Env env = Env.create();
   
    @BeforeClass
    public static void before() {
        defineGlobalEnv("int2d", int[].class);
    }

    @Test
    public void testArraySuper() {
        assertEquals(Object.class, int[].class.getSuperclass());
        assertArrayEquals(new Class[]{ Cloneable.class, Serializable.class}, int[].class.getInterfaces());
        assertArrayEquals(new Class[]{ Cloneable.class, Serializable.class}, Integer[].class.getInterfaces());
        assertFalse(Object[].class.isAssignableFrom(int[].class));
        assertTrue(Object[].class.isAssignableFrom(Integer[].class));
    }

//    @Test
//    public void testIntArray() {
//        assertArrayEquals(new int[] {0, 0}, (int[])eval(read("(define a (Array (newInstance int 2)))"), env));
//        eval(read("(Array (set a 0 100))"), env);
//        eval(read("(Array (set a 1 200))"), env);
//        assertArrayEquals(new int[] {100, 200}, (int[])eval(read("a"), env));
//    }

//    @Test
//    public void testIntArrayProcedure() {
//        assertArrayEquals(new int[] {0, 0}, (int[])eval(read("(define a (makeArray int 2))"), env));
//        assertEquals(100, eval(read("(set a 0 100)"), env));
//        assertEquals(200, eval(read("(set a 1 200)"), env));
//        assertArrayEquals(new int[] {100, 200}, (int[])eval(read("a"), env));
//        assertEquals(100, eval(read("(get a 0)"), env));
//        assertEquals(200, eval(read("(get a 1)"), env));
//    }

//    @Test
//    public void testIntegerArrayProcedure() {
//        assertArrayEquals(new Integer[] {null, null}, (Integer[])eval(read("(define a (makeArray Integer 2))"), env));
//        assertEquals(100, eval(read("(set a 0 100)"), env));
//        assertEquals(200, eval(read("(set a 1 200)"), env));
//        assertArrayEquals(new Integer[] {100, 200}, (Integer[])eval(read("a"), env));
//        assertEquals(100, eval(read("(get a 0)"), env));
//        assertEquals(200, eval(read("(get a 1)"), env));
//    }

//    @Test
//    public void testIntArray2d() {
//        assertArrayEquals(new int[][] {{0, 0},{0, 0}}, (int[][])eval(read("(define a (makeArray int 2 2))"), env));
//        assertEquals(100, eval(read("(set (get a 0) 0 100)"), env));
//        assertEquals(200, eval(read("(set (get a 0) 1 200)"), env));
//        assertArrayEquals(new int[][] {{100, 200}, {0, 0}}, (int[][])eval(read("a"), env));
//        assertEquals(100, eval(read("(get (get a 0) 0)"), env));
//        assertEquals(200, eval(read("(get (get a 0) 1)"), env));
//    }
    
    @Test
    public void testMethod() {
        assertArrayEquals(new int[] {0, 0}, (int[])eval(read("(define a (makeArray int 2))"), env));
        assertEquals(100, eval(read("(a (set 0 100))"), env));
        assertEquals(200, eval(read("(a (set 1 200))"), env));
        assertArrayEquals(new int[] {100, 200}, (int[])eval(read("a"), env));
        assertEquals(100, eval(read("(a (get 0))"), env));
        assertEquals(201, eval(read("(a (get 1) (+ 1))"), env));
    }
    
    @Test
    public void testMethod2d() {
        assertArrayEquals(new int[][] {{0, 0}, {0, 0}}, (int[][])eval(read("(define a (makeArray int 2 2))"), env));
        assertEquals(2, eval(read("(a (length))"), env));
        assertEquals(2, eval(read("(a (get 0) (length))"), env));
        assertEquals(100, eval(read("(a (get 0) (set 0 100))"), env));
        assertEquals(200, eval(read("(a (get 0) (set 1 200))"), env));
        assertArrayEquals(new int[][] {{100, 200}, {0, 0}}, (int[][])eval(read("a"), env));
        assertEquals(100, eval(read("(a (get 0) (get 0))"), env));
        assertEquals(201, eval(read("(a (get 0) (get 1) (+ 1))"), env));
    }
    
    @Test
    public void testArrayWithValues() {
        assertArrayEquals(new int[] {0, 1, 2, 3}, (int[])eval(read("(define a (array int 0 1 2 3))"), env));
        assertArrayEquals(new int[][] {{0, 1}, {2, 3}}, (int[][])eval(read(
            "(define a (array (Class (forName \"[I\"))"
            + " (array int 0 1)"
            + " (array int 2 3)))"), env));
    }

    @Test
    public void testArrayClass() throws ClassNotFoundException {
        assertEquals(Integer[].class, Class.forName("[Ljava.lang.Integer;"));
        assertEquals(Integer[][].class, Class.forName("[[Ljava.lang.Integer;"));
        assertEquals(Object.class, Integer[].class.getSuperclass());
        assertTrue(Object[].class.isAssignableFrom(Integer[].class));
        assertTrue(Object[][].class.isAssignableFrom(Integer[][].class));
        assertFalse(Object[].class.isAssignableFrom(int[].class));
        assertArrayEquals(new Object[]{Cloneable.class, Serializable.class}, Integer[].class.getInterfaces());
        assertArrayEquals(new int[]{0, 1}, new int[]{0, 1}.clone());
    }
}

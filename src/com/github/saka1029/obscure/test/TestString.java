package com.github.saka1029.obscure.test;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import com.github.saka1029.obscure.core.Env;

import static com.github.saka1029.obscure.core.Global.*;

public class TestString {

    static Env env = Env.create();
   
    @BeforeClass
    public static void before() {
        defineGlobalEnv("String", String.class);
        defineGlobalEnv("Integer", Integer.class);
    }

    @Test
    public void testStringCompare() {
//        DEBUG = true;
        assertEquals(true, eval(read("(< \"a\" \"b\")"), env));
        assertEquals(false, eval(read("(< \"b\" \"a\")"), env));
//        DEBUG = false;
    }

    @Test
    public void testStringCompareMethod() {
//        assertEquals(true, eval(read("(\"a\" (< \"b\"))"), env));
//        assertEquals(false, eval(read("(\"b\" (< \"a\"))"), env));
        assertEquals(-1, eval(read("(\"a\" (compareTo \"b\"))"), env));
    }
    
    @Test
    public void testPrint() {
        assertEquals("\"abc\"", eval(read("(\"abc\" (print))"), env));
        assertEquals("\"a\\rc\"", eval(read("(\"a\\rc\" (print))"), env));
    }
    
    @Test
    public void testMacroPrint() {
        assertEquals("\"abc\"", eval(read("(print \"abc\")"), env));
    }
    
    @Test
    public void testFormat() {
        assertEquals("a0123", eval(read("(String (format \"a%04d\" 123))"), env));
        assertEquals("abcd", eval(read("(String (format \"abcd\"))"), env));
        assertEquals(Object.class, eval(read("(import \"java.lang.Object\")"), env));
        assertArrayEquals(new Object[]{null, null}, (Object[])eval(read("(define f (makeArray Object 2))"), env));
        assertEquals("a", eval(read("(f (set 0 \"a\"))"), env));
        assertEquals(123, eval(read("(f (set 1 123))"), env));
        Object[] f = (Object[])eval(read("f"), env);
        System.out.println(String.format("%s%04d", f));
    }
    
    @Test
    public void testFormatObjectArray() {
        assertEquals(Object.class, eval(read("(import \"java.lang.Object\")"), env));
        assertArrayEquals(new Object[]{null, null}, (Object[])eval(read("(define f (makeArray Object 2))"), env));
        assertEquals("a", eval(read("(f (set 0 \"a\"))"), env));
        assertEquals(123, eval(read("(f (set 1 123))"), env));
        assertEquals("a0123", eval(read("(String (format \"%s%04d\" f))"), env));
    }
    
    @Test
    public void testFormatStringArray() {
        assertArrayEquals(new Object[]{null, null}, (Object[])eval(read("(define f (makeArray String 2))"), env));
        assertEquals("a", eval(read("(f (set 0 \"a\"))"), env));
        assertEquals("b", eval(read("(f (set 1 \"b\"))"), env));
        assertEquals("ab", eval(read("(String (format \"%s%s\" f))"), env));
    }
    
    @Test
    public void testFormatIntArray() {
        assertArrayEquals(new Object[]{null, null}, (Object[])eval(read("(define f (makeArray Integer 2))"), env));
        assertEquals(123, eval(read("(f (set 0 123))"), env));
        assertEquals(456, eval(read("(f (set 1 456))"), env));
        assertEquals("123456", eval(read("(String (format \"%d%d\" f))"), env));
    }
    
    @Test
    public void testLength() {
        assertEquals(3, eval(read("(\"abc\" (length))"), env));
    }

}

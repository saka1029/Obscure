package com.github.saka1029.obscure.test;

import static org.junit.Assert.*;


import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import com.github.saka1029.obscure.core.Env;

import static com.github.saka1029.obscure.core.Global.*;

public class TestString {

    static Env env = Env.create();
   
    @BeforeClass
    public static void before() throws IOException {
        defineGlobalEnv("String", String.class);
    }

    @Test
    public void testStringCompare() throws IOException {
        assertEquals(true, eval(read("(< \"a\" \"b\")"), env));
        assertEquals(false, eval(read("(< \"b\" \"a\")"), env));
    }

    @Test
    public void testStringCompareMethod() throws IOException {
        assertEquals(true, eval(read("(\"a\" (< \"b\"))"), env));
        assertEquals(false, eval(read("(\"b\" (< \"a\"))"), env));
        assertEquals(-1, eval(read("(\"a\" (compareTo \"b\"))"), env));
    }
    
    @Test
    public void testPrint() throws IOException {
        assertEquals("\"abc\"", eval(read("(\"abc\" (print))"), env));
        assertEquals("\"a\\rc\"", eval(read("(\"a\\rc\" (print))"), env));
    }
    
    @Test
    public void testMacroPrint() throws IOException {
        assertEquals("\"abc\"", eval(read("(print \"abc\")"), env));
    }

}

package com.github.saka1029.obscure.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.github.saka1029.obscure.core.Env;

import static com.github.saka1029.obscure.core.Global.*;

public class TestInteger {

    static Env env = Env.create();
   
    @Test
    public void testMethodsInInteger() {
        assertEquals(8, eval(read("(255 (bitCount))"), env));
        assertEquals((byte)-1, eval(read("(255 (byteValue))"), env));
        assertTrue((int)eval(read("(1 (compare 3))"), env) < 0);
        assertTrue((int)eval(read("(1 (compareTo 3))"), env) < 0);
        assertEquals(255, eval(read("(\"0xff\" (decode))"), env));
        assertEquals(12345D, eval(read("(12345 (doubleValue))"), env));
        assertEquals(255, eval(read("(255 (* 256) (* 256) (* 256) (reverse))"), env));
        assertEquals(255, eval(read("((* 255 256 256 256) (reverse))"), env));
    }
   
}

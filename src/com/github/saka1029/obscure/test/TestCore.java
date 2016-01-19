package com.github.saka1029.obscure.test;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.github.saka1029.obscure.core.Env;
import com.github.saka1029.obscure.core.Global;
import com.github.saka1029.obscure.core.Reader;

public class TestCore {

    Object read(String s) throws IOException {
        return new Reader(s).read();
    }
    
    Object eval(Object obj, Env env) {
        return Global.eval(obj, env);
    }

    @Test
    public void testClass() throws IOException {
        Env env = Env.create();
        assertEquals(String.class, eval(read("(Class (forName \"java.lang.String\"))"), env));
        assertEquals("a0123", eval(read("(Class (forName \"java.lang.String\") (format \"a%04d\" 123))"), env));
    }

    @Test
    public void testGenericOperator() throws IOException {
        Env env = Env.create();
        assertEquals(3, eval(read("(1 (+ 2))"), env));
        assertEquals(6, eval(read("(+ 1 2 3)"), env));
        assertEquals("ab", eval(read("(\"a\" (+ \"b\"))"), env));
        assertEquals("abc", eval(read("(+ \"a\" \"b\" \"c\")"), env));
    }
}

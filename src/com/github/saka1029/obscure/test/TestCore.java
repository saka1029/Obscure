package com.github.saka1029.obscure.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.github.saka1029.obscure.core.Env;
import static com.github.saka1029.obscure.core.Global.*;

public class TestCore {

    static Env env = Env.create();
    
    @Test
    public void testClass() {
        assertEquals(String.class, eval(read("(Class (forName \"java.lang.String\"))"), env));
        assertEquals("a0123", eval(read("(Class (forName \"java.lang.String\") (format \"a%04d\" 123))"), env));
    }

    @Test
    public void testGenericOperator() {
        assertEquals(3, eval(read("(1 (+ 2))"), env));
        assertEquals(6, eval(read("(+ 1 2 3)"), env));
        assertEquals("ab", eval(read("(\"a\" (+ \"b\"))"), env));
        assertEquals("abc", eval(read("(+ \"a\" \"b\" \"c\")"), env));
    }
}

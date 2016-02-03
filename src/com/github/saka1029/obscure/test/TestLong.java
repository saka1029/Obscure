package com.github.saka1029.obscure.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.github.saka1029.obscure.core.Env;

import static com.github.saka1029.obscure.core.Global.*;

public class TestLong {

    static Env env = Env.create();
    
    @Test
    public void testFact() {
        eval(read(
            "(define (fact n)"
            + "  (if (< n 1L)"
            + "      1L"
            + "      (* (fact (- n 1L)) n)))"), env);
        assertEquals(6402373705728000L, eval(read("(fact 18L)"), env));
    }
    
    @Test
    public void testLongPrint() {
        assertEquals("1234567890L", eval(read("(print 1234567890L)"), env));
    }
    
}

package com.github.saka1029.obscure.test;

import static org.junit.Assert.*;

import java.math.BigInteger;

import org.junit.Test;

import com.github.saka1029.obscure.core.Env;
import static com.github.saka1029.obscure.core.Global.*;

public class TestExtender {

    static Env env = Env.create();
    
    @Test
    public void testExtender() {
        assertEquals(3L, eval(read("(+ 1 2L)"), env));
        assertEquals(3D, eval(read("(+ 1 2D)"), env));
    }
    
    @Test
    public void testIntBigInteger() {
        assertEquals(BigInteger.valueOf(3), eval(read("(+ 1 2i)"), env));
    }

}

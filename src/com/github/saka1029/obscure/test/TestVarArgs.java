package com.github.saka1029.obscure.test;

import static org.junit.Assert.*;


import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import com.github.saka1029.obscure.core.Env;

import static com.github.saka1029.obscure.core.Global.*;

public class TestVarArgs {

    static Env env = Env.create();
   
    @BeforeClass
    public static void before() throws IOException {
        defineGlobal("String", String.class);
    }

    @Test
    public void testStringFormat() throws IOException {
        assertEquals("a0123", eval(read("(String (format \"a%04d\" 123))"), env));
        assertEquals("a0123 xyz", eval(read("(String (format \"a%04d%4s\" 123 \"xyz\"))"), env));
    }

}

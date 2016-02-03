package com.github.saka1029.obscure.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.github.saka1029.obscure.core.Env;

import static com.github.saka1029.obscure.core.Global.*;

public class TestVarArgs {

    static Env env = Env.create();
   
    @Test
    public void testImportString() {
        assertEquals(String.class, eval(read("(import \"java.lang.String\")"), env));
        assertEquals("a0123 xyz", eval(read("(String (format \"a%04d%4s\" 123 \"xyz\"))"), env));
    }
   
    @Test
    public void testImportAlias() {
        assertEquals(String.class, eval(read("(import Str \"java.lang.String\")"), env));
        assertEquals("a0123 xyz", eval(read("(Str (format \"a%04d%4s\" 123 \"xyz\"))"), env));
    }

}

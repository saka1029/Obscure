package com.github.saka1029.obscure.test;

import static org.junit.Assert.*;


import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import com.github.saka1029.obscure.core.Env;

import static com.github.saka1029.obscure.core.Global.*;
import com.github.saka1029.obscure.core.Procedure;

public class TestCast {

    static Env env = Env.create();
   
    @BeforeClass
    public static void before() throws IOException {
        defineGlobal("byte", Byte.class);
        defineGlobal("int", Integer.class);
        defineGlobal("short", Short.class);
        defineGlobal("char", Character.class);
        defineGlobal("long", Long.class);
        defineGlobal("double", Double.class);
        defineGlobal("Object", Object.class);
        defineGlobal("cast", (Procedure)(self, args) -> {
            Class<?> cls = (Class<?>)car(args);
            Object obj = car(cdr(args));
            if (cls == Integer.class)
                return ((Number)obj).intValue();
            else if (cls == Long.class)
                return ((Number)obj).longValue();
            else if (cls == Short.class)
                return ((Number)obj).shortValue();
            else if (cls == Double.class)
                return ((Number)obj).doubleValue();
            else if (cls == Byte.class)
                return ((Number)obj).byteValue();
            else if (cls == Character.class)
                return (char)((Number)obj).intValue();
            else
                return cls.cast(car(cdr(args)));
        });
        defineGlobal("asInt", (Procedure)(self, args) -> ((Number)car(args)).intValue());

        defineClass(Double.class, "int", (Procedure)(self, args) -> ((Number)self).intValue());
    }

    @Test
    public void testNumber() throws IOException {
        assertEquals(123L, eval(read("(cast long 123)"), env));
        assertEquals(123D, eval(read("(cast double 123)"), env));
    }

    @Test
    public void testChar() throws IOException {
        assertEquals('a', eval(read("(cast char 97)"), env));
    }

    @Test
    public void testCharBig() throws IOException {
        assertEquals('ÿ', eval(read("(cast char 255)"), env));
        assertEquals((char)255, eval(read("(cast char 255)"), env));
    }

    @Test
    public void testByte() throws IOException {
        assertEquals((byte)97, eval(read("(cast byte 97)"), env));
    }

    @Test
    public void testByteBig() throws IOException {
        assertEquals((byte)255, eval(read("(cast byte 255)"), env));
    }
    
    @Test
    public void testObject() throws IOException {
        assertEquals((Object)"123", eval(read("\"123\""), env));
        assertEquals((Object)"123", eval(read("(cast Object \"123\")"), env));
    }
    
    @Test(expected = ClassCastException.class)
    public void testNumberError() throws IOException {
        assertEquals(123D, eval(read("(cast double \"123\")"), env));
    }

    @Test
    public void testAsIntApplicable() throws IOException {
        assertEquals(123L, eval(read("123L"), env));
        assertEquals(123, eval(read("(asInt 123L)"), env));
        assertEquals(123.456F, eval(read("123.456F"), env));
        assertEquals(123, eval(read("(asInt 123.456F)"), env));
        assertEquals(123.456D, eval(read("123.456D"), env));
        assertEquals(123, eval(read("(asInt 123.456D)"), env));
    }

    @Test
    public void testIntMethod() throws IOException {
        assertEquals(123, eval(read("(123.456 (int))"), env));
        assertEquals(123, eval(read("(123.456D (int))"), env));
    }
}

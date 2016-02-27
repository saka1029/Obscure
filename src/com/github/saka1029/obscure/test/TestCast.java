package com.github.saka1029.obscure.test;

import static org.junit.Assert.*;

import java.math.BigInteger;

import org.junit.BeforeClass;
import org.junit.Test;

import com.github.saka1029.obscure.core.Env;
import com.github.saka1029.obscure.core.ObscureException;

import static com.github.saka1029.obscure.core.Global.*;
import com.github.saka1029.obscure.core.Procedure;

public class TestCast {

    static Env env = Env.create();
   
    @BeforeClass
    public static void before() {
        defineGlobalEnv("byte", Byte.class);
        defineGlobalEnv("int", Integer.class);
        defineGlobalEnv("short", Short.class);
        defineGlobalEnv("char", Character.class);
        defineGlobalEnv("long", Long.class);
        defineGlobalEnv("double", Double.class);
        defineGlobalEnv("Object", Object.class);
        defineGlobalEnv("BigInteger", BigInteger.class);
//        defineGlobalEnv("cast", (Procedure)(self, args) -> {
//            Class<?> cls = (Class<?>)car(args);
//            Object obj = car(cdr(args));
//            if (cls == Integer.class)
//                return ((Number)obj).intValue();
//            else if (cls == Long.class)
//                return ((Number)obj).longValue();
//            else if (cls == Short.class)
//                return ((Number)obj).shortValue();
//            else if (cls == Double.class)
//                return ((Number)obj).doubleValue();
//            else if (cls == Byte.class)
//                return ((Number)obj).byteValue();
//            else if (cls == Character.class)
//                return (char)((Number)obj).intValue();
//            else if (cls == BigInteger.class)
//                return BigInteger.valueOf(((Number)obj).longValue());
//            else
//                return cls.cast(car(cdr(args)));
//        });
        defineGlobalEnv("asInt", (Procedure)(self, args) -> ((Number)car(args)).intValue());

        defineClassEnv(Double.class, "int", (Procedure)(self, args) -> ((Number)self).intValue());
    }

    @Test
    public void testNumber() {
        assertEquals(123L, eval(read("(cast long 123)"), env));
        assertEquals(123D, eval(read("(cast double 123)"), env));
    }

    @Test
    public void testChar() {
        assertEquals('a', eval(read("(cast char 97)"), env));
    }

    @Test
    public void testCharBig() {
        assertEquals('ÿ', eval(read("(cast char 255)"), env));
        assertEquals((char)255, eval(read("(cast char 255)"), env));
    }

    @Test
    public void testByte() {
        assertEquals((byte)97, eval(read("(cast byte 97)"), env));
    }

    @Test
    public void testByteBig() {
        assertEquals((byte)255, eval(read("(cast byte 255)"), env));
    }
    
    @Test
    public void testObject() {
        assertEquals((Object)"123", eval(read("\"123\""), env));
        assertEquals((Object)"123", eval(read("(cast Object \"123\")"), env));
    }
    
    @Test(expected = ObscureException.class)
    public void testNumberError() {
        assertEquals(123D, eval(read("(cast double \"123\")"), env));
    }

    @Test
    public void testAsIntApplicable() {
        assertEquals(123L, eval(read("123L"), env));
        assertEquals(123, eval(read("(asInt 123L)"), env));
        assertEquals(123.456F, eval(read("123.456F"), env));
        assertEquals(123, eval(read("(asInt 123.456F)"), env));
        assertEquals(123.456D, eval(read("123.456D"), env));
        assertEquals(123, eval(read("(asInt 123.456D)"), env));
    }

    @Test
    public void testIntMethod() {
        assertEquals(123, eval(read("(123.456 (int))"), env));
        assertEquals(123, eval(read("(123.456D (int))"), env));
    }
    
    @Test
    public void testBigInteger() {
        assertEquals(new BigInteger("123456789012345678901234567890"), eval(read("123456789012345678901234567890I"), env));
        assertEquals(BigInteger.valueOf(1234567890L), eval(read("(cast BigInteger 1234567890L)"), env));
        assertEquals(123456789012345678901234567890D, eval(read("(cast double 123456789012345678901234567890I)"), env));
    }
}

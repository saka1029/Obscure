package com.github.saka1029.obscure.test;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import com.github.saka1029.obscure.core.Applicable;
import com.github.saka1029.obscure.core.Env;
import com.github.saka1029.obscure.core.Symbol;

import static com.github.saka1029.obscure.core.Global.*;

public class TestBoolean {

    static Env env = Env.create();
   
    @BeforeClass
    public static void before() {
        defineGlobalEnv("Boolean", String.class);
        defineGlobalEnv("if", (Applicable)(self, args, env) -> (boolean)eval(car(args), env) ? eval(cadr(args), env) : eval(caddr(args), env));
        defineClassEnv(Boolean.class, "if", (Applicable)(self, args, env) -> (boolean)self ? eval(car(args), env) : eval(cadr(args), env));
        defineClassEnv(Boolean.class, "and", (Applicable)(self, args, env) -> (boolean)self ? eval(car(args), env) : false);
        defineClassEnv(Boolean.class, "or", (Applicable)(self, args, env) -> (boolean)self ? true : eval(car(args), env));
    }

    @Test
    public void testProcedure() {
        assertEquals(1, eval(read("(if true 1 2)"), env));
        assertEquals(2, eval(read("(if false 1 2)"), env));
    }

    @Test
    public void testMethod() {
        assertEquals(4, eval(read("(true (if 1 2) (+ 3))"), env));
        assertEquals(5, eval(read("(false (if 1 2) (+ 3))"), env));
    }
    
    @Test
    public void testMethodAnd() {
        assertEquals(true, eval(read("(true (and true))"), env));
        assertEquals(false, eval(read("(true (and false))"), env));
        assertEquals(false, eval(read("(false (and true))"), env));
        assertEquals(false, eval(read("(false (and false))"), env));
    }
    
    @Test
    public void testMethodOr() {
        assertEquals(true, eval(read("(true (or true))"), env));
        assertEquals(true, eval(read("(true (or false))"), env));
        assertEquals(true, eval(read("(false (or true))"), env));
        assertEquals(false, eval(read("(false (or false))"), env));
    }

//    @Test
//    public void testMacroAnd() {
//        assertEquals(true, eval(read("(and true true)"), env));
//        assertEquals(false, eval(read("(and true false)"), env));
//        assertEquals(false, eval(read("(and false true)"), env));
//        assertEquals(false, eval(read("(and false false)"), env));
//        assertEquals(false, eval(read("(and true true true true false)"), env));
//    }
}

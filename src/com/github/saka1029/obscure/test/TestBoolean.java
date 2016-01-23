package com.github.saka1029.obscure.test;

import static org.junit.Assert.*;


import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import com.github.saka1029.obscure.core.Applicable;
import com.github.saka1029.obscure.core.Env;
import com.github.saka1029.obscure.core.GenericOperator;
import com.github.saka1029.obscure.core.Symbol;

import static com.github.saka1029.obscure.core.Global.*;

public class TestBoolean {

    static Env env = Env.create();
   
    @BeforeClass
    public static void before() throws IOException {
        defineGlobal("Boolean", String.class);
        defineGlobal("if", (Applicable)(self, args, env) -> (boolean)eval(car(args), env) ? eval(cadr(args), env) : eval(caddr(args), env));
        defineGlobal("and", new GenericOperator(Symbol.of("and")));
        defineGlobal("or", new GenericOperator(Symbol.of("or")));
        defineClass(Boolean.class, "if", (Applicable)(self, args, env) -> (boolean)self ? eval(car(args), env) : eval(cadr(args), env));
        defineClass(Boolean.class, "and", (Applicable)(self, args, env) -> (boolean)self ? eval(car(args), env) : false);
        defineClass(Boolean.class, "or", (Applicable)(self, args, env) -> (boolean)self ? true : eval(car(args), env));
    }

    @Test
    public void testProcedure() throws IOException {
        assertEquals(1, eval(read("(if true 1 2)"), env));
        assertEquals(2, eval(read("(if false 1 2)"), env));
    }

    @Test
    public void testMethod() throws IOException {
        assertEquals(4, eval(read("(true (if 1 2) (+ 3))"), env));
        assertEquals(5, eval(read("(false (if 1 2) (+ 3))"), env));
    }
    
    @Test
    public void testMethodAnd() throws IOException {
        assertEquals(true, eval(read("(true (and true))"), env));
        assertEquals(false, eval(read("(true (and false))"), env));
        assertEquals(false, eval(read("(false (and true))"), env));
        assertEquals(false, eval(read("(false (and false))"), env));
    }
    
    @Test
    public void testMethodOr() throws IOException {
        assertEquals(true, eval(read("(true (or true))"), env));
        assertEquals(true, eval(read("(true (or false))"), env));
        assertEquals(true, eval(read("(false (or true))"), env));
        assertEquals(false, eval(read("(false (or false))"), env));
    }

    @Test
    public void testMacroAnd() throws IOException {
        assertEquals(true, eval(read("(and true true)"), env));
        assertEquals(false, eval(read("(and true false)"), env));
        assertEquals(false, eval(read("(and false true)"), env));
        assertEquals(false, eval(read("(and false false)"), env));
        assertEquals(false, eval(read("(and true true true true false)"), env));
    }
}

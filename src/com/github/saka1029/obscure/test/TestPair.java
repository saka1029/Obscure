package com.github.saka1029.obscure.test;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.BeforeClass;

import static com.github.saka1029.obscure.core.Global.*;

import org.junit.Test;

import com.github.saka1029.obscure.core.Applicable;
import com.github.saka1029.obscure.core.Env;
import com.github.saka1029.obscure.core.Nil;
import com.github.saka1029.obscure.core.Procedure;
import com.github.saka1029.obscure.core.Symbol;

public class TestPair {

    static Env env = Env.create();
    
    @BeforeClass
    public static void before() {
        defineGlobal("null?", (Procedure)(self, args) -> car(args) == Nil.VALUE);
        defineGlobal("if", (Applicable)(self, args, env) -> (boolean)eval(car(args), env) ? eval(cadr(args), env) : eval(caddr(args), env));
        defineClass(Boolean.class, "if", (Applicable)(self, args, env) -> (boolean)self ? eval(car(args), env) : eval(cadr(args), env));
    }

    @Test
    public void test() throws IOException {
        assertEquals(null, read("null"));
        assertEquals(Symbol.of("null?"), read("null?"));
        assertEquals(true, eval(read("(null? '())"), env));
    }
    
    @Test
    public void testAppendApplicable() throws IOException {
        eval(read(
            "(define (append a b)"
            + "  (if (null? a)"
            + "      b"
            + "      (cons (car a)"
            + "            (append (cdr a) b)))))"), env);
        assertEquals(list(1, 2, 3, 4), eval(read("(append '(1 2) '(3 4))"), env));
    }
    
    @Test
    public void testAppendMethod() throws IOException {
        eval(read(
            "(define (append a b)"
            + "  ((null? a) (if"
            + "      b"
            + "      (cons (car a)"
            + "            (append (cdr a) b)))))"), env);
        assertEquals(list(1, 2, 3, 4), eval(read("(append '(1 2) '(3 4))"), env));
    }
    
    @Test
    public void testFact() throws IOException {
        eval(read(
            "(define (fact n)"
            + "  (if (< n 1)"
            + "      1"
            + "      (* (fact (- n 1)) n)))"), env);
        assertEquals(6, eval(read("(fact 3)"), env));
    }
    
    @Test
    public void testFactMethod() throws IOException {
        eval(read(
            "(define (fact n)"
            + "  ((n (< 1)) (if"
            + "      1"
            + "      ((fact (n (- 1))) (* n)))))"), env);
        assertEquals(6, eval(read("(fact 3)"), env));
    }

}

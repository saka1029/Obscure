package io.github.saka1029.obscure.test;

import static io.github.saka1029.obscure.core.Obscure.*;
import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import io.github.saka1029.obscure.core.Env;

public class TestObscure {

    @Test
    public void testApplicable() {
        Env env = env();
        assertEquals(sym("a"), eval(list(sym("car"), list(sym("quote"), list(sym("a"), sym("b")))), env));
        assertEquals(list(sym("b")), eval(list(sym("cdr"), list(sym("quote"), list(sym("a"), sym("b")))), env));
    }

    @Test
    public void testReader() throws IOException {
        Env env = env();
        assertEquals(read("(a b)"), eval(read("'(a b)"), env));
        assertEquals(read("a"), eval(read("(car '(a b))"), env));
        assertEquals(read("(b)"), eval(read("(cdr '(a b))"), env));
    }

    @Test
    public void testLambda() throws IOException {
        Env env = env();
        assertEquals(read("a"), eval(read("((lambda (x) (car x)) '(a b))"), env));
    }

    @Test
    public void testDefine() throws IOException {
        Env env = env();
        System.out.println(eval(read("(define kar car)"), env));
        assertEquals(read("a"), eval(read("(kar '(a b))"), env));
        System.out.println(eval(read("(define first (lambda (x) (car x)))"), env));
        assertEquals(read("a"), eval(read("(first '(a b))"), env));
        System.out.println(eval(read("(define (rest x) (cdr x))"), env));
        assertEquals(read("(b)"), eval(read("(rest '(a b))"), env));
    }
    
    @Test
    public void testString() throws IOException {
        Env env = env();
        assertEquals("abcXYZ", eval(read("(\"abc\" (+ \"XYZ\"))"), env));
        assertEquals("abcXYZ", eval(read("(+ \"ab\" \"c\" \"X\" (+ \"Y\" \"Z\"))"), env));
    }
    
    @Test
    public void testInteger() throws IOException {
        Env env = env();
        assertEquals(3, eval(read("(1 (+ 2))"), env));
        assertEquals(10, eval(read("(+ 1 2 3 (+ 2 2))"), env));
    }
}

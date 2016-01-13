package io.github.saka1029.obscure.test;

import static io.github.saka1029.obscure.core.Obscure.*;
import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import io.github.saka1029.obscure.core.Environment;

public class TestObscure {

    @Test
    public void testApplicable() {
        Environment env = env();
        assertEquals(sym("a"), eval(list(sym("car"), list(sym("quote"), list(sym("a"), sym("b")))), env));
        assertEquals(list(sym("b")), eval(list(sym("cdr"), list(sym("quote"), list(sym("a"), sym("b")))), env));
    }

    @Test
    public void testReader() throws IOException {
        Environment env = env();
        assertEquals(read("(a b)"), eval(read("'(a b)"), env));
        assertEquals(read("a"), eval(read("(car '(a b))"), env));
        assertEquals(read("(b)"), eval(read("(cdr '(a b))"), env));
    }

    @Test
    public void testLambda() throws IOException {
        Environment env = env();
        assertEquals(read("a"), eval(read("((lambda (x) (car x)) '(a b))"), env));
    }

    @Test
    public void testDefine() throws IOException {
        Environment env = env();
        System.out.println(eval(read("(define kar car)"), env));
        assertEquals(read("a"), eval(read("(kar '(a b))"), env));
        System.out.println(eval(read("(define first (lambda (x) (car x)))"), env));
        assertEquals(read("a"), eval(read("(first '(a b))"), env));
        System.out.println(eval(read("(define (rest x) (cdr x))"), env));
        assertEquals(read("(b)"), eval(read("(rest '(a b))"), env));
    }
}

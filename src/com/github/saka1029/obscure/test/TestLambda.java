package com.github.saka1029.obscure.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.github.saka1029.obscure.core.Env;
import static com.github.saka1029.obscure.core.Global.*;

public class TestLambda {

    static Env env = Env.create();
    
    @Test
    public void test() {
        assertEquals(1, eval(read("(car '(1 2))"), env));
        assertEquals(1, eval(read("((lambda (x) (car x)) '(1 2))"), env));
        eval(read("(define kar (lambda (x) (car x)))"), env);
        assertEquals(1, eval(read("(kar '(1 2))"), env));
        eval(read("(define (qar x) (car x))"), env);
        assertEquals(1, eval(read("(qar '(1 2))"), env));
    }
    
    @Test
    public void testVarargs() {
        eval(read("(define (list . b) b)"), env);
        assertEquals(list(1, 2, 3), eval(read("(list 1 2 3)"), env));
    }
    
    @Test
    public void testVarargsLambda() {
        eval(read("(define list (lambda x x))"), env);
        assertEquals(list(1, 2, 3), eval(read("(list 1 2 3)"), env));
    }
    
    @Test
    public void testPrintf() {
        eval(read("(define (format f . args) (String (format f (vargs args))))"), env);
        assertEquals("a0123", eval(read("(format \"%s%04d\" \"a\" 123)"), env));
        assertEquals("01230004", eval(read("(format \"%04d%04d\" 123 4)"), env));
    }

}

package obscure.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

import obscure.core.Env;
import obscure.core.Global;
import static obscure.core.ListHelper.*;
import obscure.core.Reader;

public class TestObscure {

    static Object read(String s) throws IOException {
        return new Reader(new StringReader(s)).read();
    }

    static Object evalRead(String s, Env env) throws IOException {
        Object input = read(s);
        Object output = Global.eval(input, env);
        System.out.printf("%s -> %s%n", Global.print(input), Global.print(output));
        return output;
    }

    @Test
    public void testRead() throws IOException {
        Env env = Env.create();
        assertEquals(list(sym("a"), sym("b")), evalRead("'(a b)", env));
        assertEquals(sym("a"), evalRead("(car '(a b))", env));
        assertEquals("abcdef", evalRead("(concat \"abc\" \"def\")", env));
        assertEquals(cons(sym("a"), sym("b")), evalRead("(cons 'a 'b))", env));
        assertEquals(String.class, evalRead("(getClass \"\")", env));
        assertEquals(String.class, evalRead("(forName Class \"java.lang.String\")", env));
    }
    
    @Test
    public void testCascade() throws IOException {
        Env env = Env.create();
        assertEquals(StringBuilder.class, evalRead("(define StringBuilder (forName Class \"java.lang.StringBuilder\"))", env));
        assertEquals("abc123", evalRead("(toString (append (append (new StringBuilder) \"abc\") 123))", env));
        assertEquals("abc123", evalRead("(; (new StringBuilder) (append \"abc\") (append 123) (toString))", env));
    }
    
    @Test
    public void testStaticMethod() throws IOException {
        Env env = Env.create();
        assertEquals(String.class, evalRead("(define String (getClass \"\"))", env));
        assertEquals("a0001", evalRead("(format String \"a%04d\" 1)", env));
        assertEquals("f123x", evalRead("(format String \"f%d%s\" 123 \"x\")", env));
    }

    @Test
    public void testIntegerWrapper() throws IOException {
        Env env = Env.create();
        assertEquals(100, evalRead("(define x 100)", env));
        assertEquals(6, evalRead("(+ 1 2 3)", env));
        assertEquals(10, evalRead("(+ 1 (+ 2 3) 4)", env));
        assertEquals(111, evalRead("(+ 1 (* 2 3) 4 x)", env));
    }

    @Test
    public void testStringWrapper() throws IOException {
        Env env = Env.create();
        assertEquals("a123b", evalRead("(+ \"a\" 123 'b)", env));
        assertEquals("a123b", evalRead("(+ \"a\" (+ 100 23) 'b)", env));
    }

    public static class Person {
        
        public String name;

        public Person(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public String greeting(String x) {
            return x + " " + name;
        }
        
        public static final int NO = 123;
        
        public static int plusOne(int n) {
            return n + 1;
        }

    }

    @Test
    public void testPerson() throws IOException {
        Env env = Env.create();
//        env.define(sym("Person"), Person.class);
        assertEquals(Person.class, evalRead("(define Person (forName Class \"" + Person.class.getName() + "\"))", env));
        // constructor
        evalRead("(define p (new Person \"Jhon\"))", env);
        // field
        assertEquals("Jhon", evalRead("(@ p name)", env));
        // method
        assertEquals("Jhon", evalRead("(getName p)", env));
        assertEquals("Hello Jhon", evalRead("(greeting p \"Hello\")", env));
        // static field
        assertEquals(123, evalRead("(@ Person NO)", env));
        // static method
        assertEquals(4, evalRead("(plusOne Person (+ 1 2))", env));
    }
    
    @Test
    public void testIntArray() throws IOException {
        Env env = Env.create();
        assertEquals(int[].class, evalRead("(forName Class \"[I\")", env));
        assertEquals(Integer.class, evalRead("(define Integer (forName Class \"java.lang.Integer\"))", env));
        assertEquals(int.class, evalRead("(define int (@ (forName Class \"java.lang.Integer\") TYPE))", env));
        evalRead("(define array (forName Class \"java.lang.reflect.Array\"))", env);
        assertArrayEquals(new int[] {0, 0}, (int[])evalRead("(define intArray (newInstance array int 2))", env));
        assertEquals(null, evalRead("(set array intArray 0 100)", env));
        assertEquals(null, evalRead("(set array intArray 1 200)", env));
        assertArrayEquals(new int[] {100, 200}, (int[])evalRead("intArray", env));
        assertArrayEquals(new Integer[] {null, null}, (Integer[])evalRead("(define integerArray (newInstance array Integer 2))", env));
        assertEquals(null, evalRead("(set array integerArray 0 10)", env));
        assertEquals(null, evalRead("(set array integerArray 1 20)", env));
        assertEquals(20, evalRead("(get array integerArray 1)", env));
        assertArrayEquals(new Integer[] {10, 20}, (Integer[])evalRead("integerArray", env));
        assertArrayEquals(new int[][] {{0, 0}, {0, 0}}, (int[][])evalRead("(define matrix (newInstance array int 2 2))", env));
        assertEquals(null, evalRead("(set array (get array matrix 0) 0 0)", env));
        assertEquals(null, evalRead("(set array (get array matrix 0) 1 1)", env));
        assertEquals(null, evalRead("(set array (get array matrix 1) 0 2)", env));
        assertEquals(null, evalRead("(set array (get array matrix 1) 1 3)", env));
        assertArrayEquals(new int[][] {{0, 1}, {2, 3}}, (int[][])evalRead("matrix", env));
    }
    
    static String print(Object obj) {
        return Global.print(obj);
    }

    @Test
    public void testPrintString() {
        assertEquals("\"abc\"", print("abc"));
        assertEquals("\"a \\\"quoted\\\" c\"", print("a \"quoted\" c"));
        assertEquals("\"a\\rb\\nc\"", print("a\rb\nc"));
    }
    
    @Test
    public void testLambda() throws IOException {
        Env env = Env.create();
        assertEquals(sym("a"), evalRead("((lambda (x) (car x)) '(a b))", env));
        assertEquals(list(sym("a"), sym("b")), evalRead("((lambda x x) 'a 'b)", env));
        evalRead("(define kar (lambda (x) (car x)))", env);
        assertEquals(sym("a"), evalRead("(kar '(a b))", env));
        evalRead("(define (first x) (car x))", env);
        assertEquals(sym("a"), evalRead("(first '(a b))", env));
        evalRead("(define (LIST . x) x)", env);
        assertEquals(list(sym("a"), sym("b")), evalRead("(LIST 'a 'b)", env));
    }
    
    @Test
    public void testMacro() throws IOException {
        Env env = Env.create();
        evalRead("(define (list . x) x)", env);
        evalRead("(define kar (macro (x) (list 'car (list 'quote x))))", env);
        assertEquals(sym("a"), evalRead("(kar (a b))", env));
        evalRead("(defmacro (CAR x) (list 'car (list 'quote x)))", env);
        assertEquals(sym("a"), evalRead("(CAR (a b))", env));
    }
    
    @Test
    public void testExpand() throws IOException {
        Env env = Env.create();
        assertEquals(read("((lambda (x y) (cons x y)) 0 1)"), evalRead("(expand (let ((x 0) (y 1)) (cons x y)))", env));
    }
    
    @Test
    public void testLet() throws IOException {
        Env env = Env.create();
        assertEquals(cons(0, 1), evalRead("(let ((x 0) (y 1)) (cons x y))", env));
    }
}

package obscure.test;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import obscure.core.Env;
import obscure.core.Global;
import obscure.core.ObscureException;

import static obscure.core.Helper.*;
import obscure.core.Reader;

public class TestObscure {

    static Object read(String s) throws IOException {
        return new Reader(s).read();
    }

    static Object evalRead(String s, Env env) throws IOException {
        Object input = read(s);
        Object output = eval(input, env);
        System.out.printf("%s -> %s%n", print(input), print(output));
        return output;
    }

    @Test
    public void testRead() throws IOException {
        Env env = Env.create();
        assertEquals(list(sym("a"), sym("b")), evalRead("'(a b)", env));
        assertEquals(sym("a"), evalRead("(car '(a b))", env));
        assertEquals("abcdef", evalRead("(\"abc\" concat \"def\")", env));
        assertEquals(cons(sym("a"), sym("b")), evalRead("(cons 'a 'b))", env));
        assertEquals(String.class, evalRead("(\"\" getClass)", env));
        assertEquals(String.class, evalRead("(Class forName \"java.lang.String\")", env));
    }
    
    @Test
    public void testCascade() throws IOException {
        Env env = Env.create();
        assertEquals(StringBuilder.class, evalRead("(define StringBuilder (Class forName \"java.lang.StringBuilder\"))", env));
        assertEquals("abc123", evalRead("((((StringBuilder new) append \"abc\") append 123) toString)", env));
        assertEquals("abc123", evalRead("(; (StringBuilder new) (append \"abc\") (append 123) (toString))", env));
    }
    
    @Test
    public void testStaticMethod() throws IOException {
        Env env = Env.create();
        assertEquals(String.class, evalRead("(define String (\"\" getClass))", env));
        assertEquals("a0001", evalRead("(String format \"a%04d\" 1)", env));
        assertEquals("f123x", evalRead("(String format \"f%d%s\" 123 \"x\")", env));
    }

    @Test
    public void testIntegerWrapper() throws IOException {
        Env env = Env.create();
        assertEquals(100, evalRead("(define x 100)", env));
        assertEquals(6, evalRead("(1 + 2 3)", env));
        assertEquals(10, evalRead("(1 + (2 + 3) 4)", env));
        assertEquals(111, evalRead("(1 + (2 * 3) 4 x)", env));
    }

    @Test
    public void testStringWrapper() throws IOException {
        Env env = Env.create();
        assertEquals("a123b", evalRead("(\"a\" + 123 'b)", env));
        assertEquals("a123b", evalRead("(\"a\" + (100 + 23) 'b)", env));
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
        assertEquals(Person.class, evalRead("(define Person (Class forName \"" + Person.class.getName() + "\"))", env));
        // constructor
        evalRead("(define p (Person new \"Jhon\"))", env);
        // field
        assertEquals("Jhon", evalRead("(p . name)", env));
        // method
        assertEquals("Jhon", evalRead("(p getName)", env));
        assertEquals("Hello Jhon", evalRead("(p greeting \"Hello\")", env));
        // static field
        assertEquals(123, evalRead("(Person . NO)", env));
        // static method
        assertEquals(4, evalRead("(Person plusOne (1 + 2))", env));
    }
    
    @Test
    public void testIntArray() throws IOException {
        Env env = Env.create();
        assertEquals(int[].class, evalRead("(Class forName \"[I\")", env));
        assertEquals(Integer.class, evalRead("(define Integer (Class forName \"java.lang.Integer\"))", env));
        assertEquals(int.class, evalRead("(define int ((Class forName \"java.lang.Integer\") . TYPE))", env));
        evalRead("(define array (Class forName \"java.lang.reflect.Array\"))", env);
        assertArrayEquals(new int[] {0, 0}, (int[])evalRead("(define intArray (array newInstance int 2))", env));
        assertEquals(null, evalRead("(array set intArray 0 100)", env));
        assertEquals(null, evalRead("(array set intArray 1 200)", env));
        assertArrayEquals(new int[] {100, 200}, (int[])evalRead("intArray", env));
        assertArrayEquals(new Integer[] {null, null}, (Integer[])evalRead("(define integerArray (array newInstance Integer 2))", env));
        assertEquals(null, evalRead("(array set integerArray 0 10)", env));
        assertEquals(null, evalRead("(array set integerArray 1 20)", env));
        assertEquals(20, evalRead("(array get integerArray 1)", env));
        assertArrayEquals(new Integer[] {10, 20}, (Integer[])evalRead("integerArray", env));
        assertArrayEquals(new int[][] {{0, 0}, {0, 0}}, (int[][])evalRead("(define matrix (array newInstance int 2 2))", env));
        assertEquals(null, evalRead("(array set (array get matrix 0) 0 0)", env));
        assertEquals(null, evalRead("(array set (array get matrix 0) 1 1)", env));
        assertEquals(null, evalRead("(array set (array get matrix 1) 0 2)", env));
        assertEquals(null, evalRead("(array set (array get matrix 1) 1 3)", env));
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
        evalRead("(define (list . x) x)", env);
        assertEquals(list(sym("a"), sym("b")), evalRead("(list 'a 'b)", env));
        evalRead("(define (foo a b . x) (list a b x))", env);
        assertEquals(list(sym("a"), sym("b"), list(sym("c"), sym("d"))), evalRead("(foo 'a 'b 'c 'd)", env));
    }
    
    @Test
    public void testMacro() throws IOException {
        Env env = Env.create();
        evalRead("(define (list . x) x)", env);
        evalRead("(define kar (macro (x) (list 'car (list 'quote x))))", env);
        assertEquals(sym("a"), evalRead("(kar (a b))", env));
        evalRead("(define-macro (CAR x) (list 'car (list 'quote x)))", env);
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
        assertEquals(100, evalRead("(define z 100)", env));
        assertEquals(list(0, 1, 2), evalRead("(let ((x 0) (y 1)) (define z 2) (list x y z))", env));
        assertEquals(list(0, 1, 100), evalRead("(let ((x 0) (y 1)) (list x y z))", env));
        assertEquals(100, evalRead("z", env));
    }

    @Test
    public void testAppend() throws IOException {
        Env env = Env.create();
        assertEquals(list(0, 1, 2, 3), evalRead("('(0 1) + '(2 3))", env));
    }
    
    @Test
    public void testAddMacro() throws IOException {
        Env env = Env.create();
        assertEquals(100, evalRead("(define x 100)", env));
        assertEquals(111, evalRead("(+ 1 (* 2 3) 4 x)", env));
        assertEquals("a123b", evalRead("(+ \"a\" (+ 100 23) 'b)", env));
        assertEquals(list(0, 1, 2, 3), evalRead("(+ '(0 1) '(2 3))", env));
    }
    
    @Test
    public void testReader() throws IOException {
        assertEquals("first\r\nsecond", read("\"first\\r\\nsecond\""));
        assertEquals("He said \"Yes.\".", read("\"He said \\\"Yes.\\\".\""));
        assertEquals("\"He said \\\"Yes.\\\".\"", print(read("\"He said \\\"Yes.\\\".\"")));
        assertEquals('\r', read("?\\r"));
        assertEquals("?\\r", print(read("?\\r")));
    }
    
    @Test(expected = ObscureException.class)
    public void testReaderUnterminatedString() throws IOException {
        assertEquals("first\r\nsecond", read("\"first\r\nsecond\""));
    }

    static int foo() { return 1; }
}

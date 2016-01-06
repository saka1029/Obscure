package obscure.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

import obscure.core.Env;
import obscure.core.Global;
import obscure.core.Pair;
import obscure.core.Reader;
import obscure.core.Symbol;

public class TestObscure {

    static Symbol sym(String s) {
        return Symbol.of(s);
    }

    static Object read(String s, Env env) throws IOException {
        Object input = new Reader(new StringReader(s)).read();
        Object output = Global.eval(input, env);
        System.out.printf("%s -> %s%n", Global.print(input), Global.print(output));
        return output;
    }

    @Test
    public void testRead() throws IOException {
        Env env = Env.create();
        assertEquals(Pair.list(sym("a"), sym("b")), read("'(a b)", env));
        assertEquals(sym("a"), read("(car '(a b))", env));
        assertEquals("abcdef", read("(concat \"abc\" \"def\")", env));
        assertEquals(Pair.of(sym("a"), sym("b")), read("(cons 'a 'b))", env));
        assertEquals(String.class, read("(getClass \"\")", env));
        assertEquals(String.class, read("(forName Class \"java.lang.String\")", env));
    }
    
    @Test
    public void testCascade() throws IOException {
        Env env = Env.create();
        assertEquals(StringBuilder.class, read("(define StringBuilder (forName Class \"java.lang.StringBuilder\"))", env));
        assertEquals("abc123", read("(toString (append (append (new StringBuilder) \"abc\") 123))", env));
        assertEquals("abc123", read("(; (new StringBuilder) (append \"abc\") (append 123) (toString))", env));
    }
    
    @Test
    public void testStaticMethod() throws IOException {
        Env env = Env.create();
        assertEquals(String.class, read("(define String (getClass \"\"))", env));
        assertEquals("a0001", read("(format String \"a%04d\" 1)", env));
        assertEquals("f123x", read("(format String \"f%d%s\" 123 \"x\")", env));
    }

    @Test
    public void testIntegerWrapper() throws IOException {
        Env env = Env.create();
        assertEquals(100, read("(define x 100)", env));
        assertEquals(6, read("(+ 1 2 3)", env));
        assertEquals(10, read("(+ 1 (+ 2 3) 4)", env));
        assertEquals(111, read("(+ 1 (* 2 3) 4 x)", env));
    }

    @Test
    public void testStringWrapper() throws IOException {
        Env env = Env.create();
        assertEquals("a123b", read("(+ \"a\" 123 'b)", env));
        assertEquals("a123b", read("(+ \"a\" (+ 100 23) 'b)", env));
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
        assertEquals(Person.class, read("(define Person (forName Class \"" + Person.class.getName() + "\"))", env));
        // constructor
        read("(define p (new Person \"Jhon\"))", env);
        // field
        assertEquals("Jhon", read("(@ p name)", env));
        // method
        assertEquals("Jhon", read("(getName p)", env));
        assertEquals("Hello Jhon", read("(greeting p \"Hello\")", env));
        // static field
        assertEquals(123, read("(@ Person NO)", env));
        // static method
        assertEquals(4, read("(plusOne Person (+ 1 2))", env));
    }
    
    @Test
    public void testIntArray() throws IOException {
        Env env = Env.create();
        assertEquals(int[].class, read("(forName Class \"[I\")", env));
        assertEquals(Integer.class, read("(define Integer (forName Class \"java.lang.Integer\"))", env));
        assertEquals(int.class, read("(define int (@ (forName Class \"java.lang.Integer\") TYPE))", env));
        read("(define array (forName Class \"java.lang.reflect.Array\"))", env);
        assertArrayEquals(new int[] {0, 0}, (int[])read("(define intArray (newInstance array int 2))", env));
        assertEquals(null, read("(set array intArray 0 100)", env));
        assertEquals(null, read("(set array intArray 1 200)", env));
        assertArrayEquals(new int[] {100, 200}, (int[])read("intArray", env));
        assertArrayEquals(new Integer[] {null, null}, (Integer[])read("(define integerArray (newInstance array Integer 2))", env));
        assertEquals(null, read("(set array integerArray 0 10)", env));
        assertEquals(null, read("(set array integerArray 1 20)", env));
        assertEquals(20, read("(get array integerArray 1)", env));
        assertArrayEquals(new Integer[] {10, 20}, (Integer[])read("integerArray", env));
        assertArrayEquals(new int[][] {{0, 0}, {0, 0}}, (int[][])read("(define matrix (newInstance array int 2 2))", env));
        assertEquals(null, read("(set array (get array matrix 0) 0 0)", env));
        assertEquals(null, read("(set array (get array matrix 0) 1 1)", env));
        assertEquals(null, read("(set array (get array matrix 1) 0 2)", env));
        assertEquals(null, read("(set array (get array matrix 1) 1 3)", env));
        assertArrayEquals(new int[][] {{0, 1}, {2, 3}}, (int[][])read("matrix", env));
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
        assertEquals(Symbol.of("a"), read("((lambda (x) (car x)) '(a b))", env));
        assertEquals(Pair.list(Symbol.of("a"), Symbol.of("b")), read("((lambda x x) 'a 'b)", env));
        read("(define kar (lambda (x) (car x)))", env);
        assertEquals(Symbol.of("a"), read("(kar '(a b))", env));
        read("(define (first x) (car x))", env);
        assertEquals(Symbol.of("a"), read("(first '(a b))", env));
        read("(define (LIST . x) x)", env);
        assertEquals(Pair.list(Symbol.of("a"), Symbol.of("b")), read("(LIST 'a 'b)", env));
    }
}

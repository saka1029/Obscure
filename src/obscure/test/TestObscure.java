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
        Object evaled = Global.eval(input, env);
        System.out.printf("%s -> %s%n", input, evaled);
        return evaled;
    }

    @Test
    public void testRead() throws IOException {
        Env env = Env.create();
        assertEquals(sym("a"), read("(car '(a b))", env));
        assertEquals("abcdef", read("(concat \"abc\" \"def\")", env));
        assertEquals(Pair.of(sym("a"), sym("b")), read("(cons 'a 'b))", env));
        assertEquals(String.class, read("(getClass \"\")", env));
        assertEquals(String.class, read("(forName Class \"java.lang.String\")", env));
    }
    
    @Test
    public void testStaticMethod() throws IOException {
        Env env = Env.create();
        assertEquals(String.class, read("(define String (getClass \"\"))", env));
        assertEquals("a0001", read("(format String \"a%04d\" 1)", env));
    }

    @Test
    public void testIntegerWrapper() throws IOException {
        Env env = Env.create();
        assertEquals(6, read("(+ 1 2 3)", env));
        assertEquals(10, read("(+ 1 (+ 2 3) 4)", env));
        assertEquals(11, read("(+ 1 (* 2 3) 4)", env));
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
        
        public static int increment(int n) {
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
        assertEquals("Jhon", read("(get p name)", env));
        // method
        assertEquals("Jhon", read("(getName p)", env));
        assertEquals("Hello Jhon", read("(greeting p \"Hello\")", env));
        // static field
        assertEquals(123, read("(get Person NO)", env));
        // static method
        assertEquals(4, read("(increment Person (+ 1 2))", env));
    }
    
}

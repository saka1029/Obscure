package obscure.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

import obscure.core.Env;
import obscure.core.Nil;
import obscure.core.Obj;
import obscure.core.ObscureException;
import obscure.core.Pair;
import obscure.core.Reader;
import obscure.core.Symbol;
import obscure.primitive.Car;
import obscure.primitive.Cdr;
import obscure.syntax.Lambda;
import obscure.syntax.Quote;

public class TestObscure {

    static Symbol sym(String s) {
        return Symbol.of(s);
    }

    static Obj read(String s) throws IOException {
        return new Reader(new StringReader(s)).read();
    }

    void testReader(String expected, String s) throws IOException {
        assertEquals(expected, read(s).toString());
    }

    @Test
    public void testReader() throws IOException {
        testReader("a", "a");
        testReader("@a", "@a");
        testReader("#a", "#a");
        testReader("$a", "$a");
        testReader("漢字", "漢字");
        testReader("java.lang.0", "java.lang.0");
        testReader("'a", "'a");
        testReader("'a", "(quote a)");
        testReader("(a a)", "(a a)");
        testReader("'(a b)", "(quote (a b))");
        testReader("(quote (a b) c)", "(quote (a b) c)");
        testReader("(quote . a)", "(quote . a)");
    }
    
    @Test(expected = ObscureException.class)
    public void testReaderUnknownError() throws IOException {
        read(")");
    }
    
    @Test(expected = ObscureException.class)
    public void testReaderDotError() throws IOException {
        read("( . a)");
    }
    
    @Test(expected = ObscureException.class)
    public void testReaderDot2Error() throws IOException {
        read("(a . a . b)");
    }
    
    void testEval(String expected, String s, Env env) throws IOException {
        assertEquals(expected, read(s).eval(env).toString());
    }

    @Test
    public void testPair() {
        assertEquals("(a . b)", Pair.of(sym("a"), sym("b")).toString());
        
    }

    @Test
    public void testPairBuilder() {
        assertEquals("(a b)", Pair.builder().tail(sym("a")).tail(sym("b")).build().toString());
        assertEquals("(a b)", Pair.builder().head(sym("b")).head(sym("a")).build().toString());
    }

    @Test(expected = ObscureException.class)
    public void testPairBuilderError() {
        Pair.builder().end(sym("a"));
    }
    
    @Test
    public void testCar() throws IOException {
        Env env = Env.create();
        env.define(sym("quote"), new Quote());
        env.define(sym("car"), new Car());
        env.define(sym("cdr"), new Cdr());
        testEval("a", "(car '(a . b))", env);
        testEval("b", "(cdr '(a . b))", env);
        testEval("()", "(cdr '(a))", env);
    }

    @Test
    public void testLambda() throws IOException {
        Env env = Env.create();
        env.define(sym("quote"), new Quote());
        env.define(sym("car"), new Car());
        env.define(sym("cdr"), new Cdr());
        env.define(sym("lambda"), new Lambda());
        testEval("a", "((lambda (x) (car x)) '(a . b))", env);
        testEval("a", "((lambda x (car (car x))) '(a . b))", env);
        testEval("(b)", "((lambda (x . y) y) 'a 'b)", env);
    }

    @Test(expected = ObscureException.class)
    public void testObjCarError() {
        sym("a").car();
    }

    @Test(expected = ObscureException.class)
    public void testObjCdrError() {
        sym("a").cdr();
    }

    @Test(expected = ObscureException.class)
    public void testObjEvalError() {
        Env env = Env.create();
        Nil.value.eval(env);
    }

    @Test(expected = ObscureException.class)
    public void testObjApplyError() {
        Env env = Env.create();
        Nil.value.apply(Nil.value, env);
    }
    
    @Test
    public void testCons() throws IOException {
        Env env = Env.create();
        testEval("(a . b)", "(cons 'a 'b)", env);
    }
    
    @Test
    public void testEquals() throws IOException {
        Env env = Env.create();
        testEval("false", "(equals 'a 'b)", env);
        testEval("true", "(equals 'a 'a)", env);
        testEval("true", "(equals '(a b) '(a b))", env);
    }
    
    @Test
    public void testSet() throws IOException {
        Env env = Env.create();
        testEval("(a)", "(define a '(a))", env);
        testEval("(b)", "(set a '(b))", env);
        testEval("(b)", "a", env);
    }

    @Test(expected = ObscureException.class)
    public void testSetError() throws IOException {
        Env env = Env.create();
        testEval("(a)", "(set a '(a))", env);
    }

    @Test(expected = ObscureException.class)
    public void testSetNotSymbolError() throws IOException {
        Env env = Env.create();
        testEval("(a)", "(set (foo) '(a))", env);
    }

    @Test
    public void testClass() throws IOException {
        Env env = Env.create();
        testEval("who", "(define name 'who)", env);
        read("(define (Person name) (define (greeting x) (list x name)) (new))").eval(env);
        read("(define jhon (Person 'JhonDoe))").eval(env);
        testEval("JhonDoe", "(jhon name)", env);
        testEval("JhonLennon", "(jhon (set name 'JhonLennon))", env);
        testEval("JhonLennon", "(jhon name)", env);
        testEval("(Hello JhonLennon)", "(jhon (greeting 'Hello))", env);
        testEval("who", "name", env);
        read("(define w (Person name))").eval(env);
        testEval("who", "(w name)", env);
    }

    @Test(expected = ObscureException.class)
    public void testClosureError() throws IOException {
        Env env = Env.create();
        testEval("a", "(lambda ((x)) 'a)", env);
    }

}

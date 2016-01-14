package io.github.saka1029.obscure.test;

import static org.junit.Assert.*;

import java.io.IOException;

import static io.github.saka1029.obscure.core.Obscure.*;

import org.junit.Test;

import io.github.saka1029.obscure.core.Env;

public class TestMethod {

    @Test
    public void testMethod() throws IOException {
        Env env = env();
        assertEquals(3, eval(read("(\"abc\" (length))"), env));
        assertEquals("abcdef", eval(read("(\"abc\" (concat \"def\"))"), env));
        assertEquals(6, eval(read("(\"abc\" (concat \"def\") (length))"), env));
        eval(read("(define String (Class (forName \"java.lang.String\")))"), env);
        assertEquals("a123", eval(read("(String (format \"a%d\" 123))"), env));
    }
    
    public static class Person {

        public static int FIELD = 123;
        
        public static Person of(String name) {
            return new Person(name);
        }
        
        public String name;
        
        public Person(String name) {
            this.name = name;
        }
        
        public String getName() {
            return name;
        }

        public String greeting(String prefix) {
            return prefix + " " + name;
        }
    }
    
    @Test
    public void testPerson() throws IOException {
        Env env = env();
        assertEquals(Person.class, eval(read("(define Person (Class (forName \"" + Person.class.getName() + "\")))"), env));
        eval(read("(define jhon (Person (new \"Jhon\")))"), env);
        assertEquals("Jhon", eval(read("(jhon name)"), env));
        assertEquals("Jhon", eval(read("(jhon (getName))"), env));
        assertEquals("Hello Jhon", eval(read("(jhon (greeting \"Hello\"))"), env));
        assertEquals(123, eval(read("(Person FIELD)"), env));
        assertEquals("Jane", eval(read("((Person (of \"Jane\")) name)"), env));
        assertEquals("Jane", eval(read("(Person (of \"Jane\") name)"), env));
    }

}

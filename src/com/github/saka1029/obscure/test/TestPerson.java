package com.github.saka1029.obscure.test;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.github.saka1029.obscure.core.Env;
import com.github.saka1029.obscure.core.Global;
import com.github.saka1029.obscure.core.Reader;

public class TestPerson {

    Object read(String s) throws IOException {
        return new Reader(s).read();
    }
    
    Object eval(Object obj, Env env) {
        return Global.eval(obj, env);
    }

    public static class Person {

        public String name;
        
        public Person(String name) {
            this.name = name;
        }
        
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        @Override
        public String toString() {
            return String.format("Person(%s)", name);
        }
        
        public static int VERSION = 2;
        
        public static Person of(String name) {
            return new Person(name);
        }

    }

    @Test
    public void test() throws IOException {
        Env env = Env.create();
        eval(read("(define Person (Class (forName \"" + Person.class.getName() + "\")))"), env);
        eval(read("(define jhon (Person (new \"Jhon\")))"), env);
        assertEquals("Jhon", eval(read("(jhon name)"), env));
        assertEquals("Jhon", eval(read("(jhon (getName))"), env));
        assertEquals("Person(Jhon)", eval(read("(jhon (toString))"), env));
        assertEquals(2, eval(read("(Person VERSION)"), env));
        assertEquals("Person(Jane)", eval(read("(Person (of \"Jane\") (toString))"), env));
    }

}

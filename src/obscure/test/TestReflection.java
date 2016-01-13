package obscure.test;

import static org.junit.Assert.*;

import java.util.Objects;

import org.junit.Test;

import obscure.core.Reflection;

public class TestReflection {

    public static class Person {

        public static int VERSION = 123;
        
        public String name;
        
        public Person(String name) {
            this.name = name;
        }
        
        public String greeting(String prefix) {
            return prefix + " " + name;
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(name);
        }
        
        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Person))
                return false;
            return Objects.equals(name, ((Person)obj).name);
        }
    }

    @Test
    public void testInstanceField() {
        assertEquals("Jhon", Reflection.field(new Person("Jhon"), "name"));
        assertEquals(Reflection.NOT_FOUND, Reflection.field(new Person("Jhon"), "address"));
    }

    @Test
    public void testStaticField() {
        assertEquals(123, Reflection.field(new Person("Jhon"), "VERSION"));
        assertEquals(123, Reflection.field(Person.class, "VERSION"));
    }
    
    @Test
    public void testConstructor() {
        assertEquals(new Person("Jhon"), Reflection.constructor(Person.class, "Jhon"));
        assertEquals(Reflection.NOT_FOUND, Reflection.constructor(Person.class));
    }

    @Test
    public void testInstanceMethod() {
        assertEquals("Hello Jhon", Reflection.method(new Person("Jhon"), "greeting", "Hello"));
        assertEquals(Reflection.NOT_FOUND, Reflection.method(new Person("Jhon"), "hello", "Hello"));
    }

}

package com.github.saka1029.obscure.test;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

import org.junit.Test;

import static com.github.saka1029.obscure.core.Global.*;
import com.github.saka1029.obscure.core.Env;
import com.github.saka1029.obscure.core.List;
import com.github.saka1029.obscure.core.Procedure;

public class TestProxy {

    @Test
    public void testReflect() {
        Class<?> c = BiConsumer.class;
        Method[] abstracts = Stream.of(c.getMethods())
            .filter(m -> (m.getModifiers() & Modifier.ABSTRACT) != 0)
            .toArray(Method[]::new);
        assertEquals(1, abstracts.length);
        assertEquals(2, abstracts[0].getParameterCount());
        assertEquals("accept", abstracts[0].getName());
    }

    @Test
    public void testProxy() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        Class<?> c = BiConsumer.class;
        Object proxy = Proxy.newProxyInstance(
            getClass().getClassLoader(), new Class<?>[]{c},
            (self, method, args) -> {
                assertArrayEquals(new Object[] {"abc", 123}, args);
                return null;
            });
        Method m = proxy.getClass().getMethod("accept", new Class<?>[]{Object.class, Object.class});
        m.invoke(proxy, "abc", 123);
        assertEquals(Proxy.class, proxy.getClass().getSuperclass());
    }
    
    @Test
    public void testApplicable() {
        Env env = Env.create();
        Procedure proc = (self, args) -> {
            assertEquals(list("abc", 123), args);
            return "result";
        };
        Object proxy = Proxy.newProxyInstance(getClass().getClassLoader(),
            new Class<?>[]{BiFunction.class},
                (self, method, args) -> proc.apply(self, list(args))
            );
        assertTrue(proxy instanceof BiFunction);
        ((BiFunction<String, Integer, String>)proxy).apply("abc", 123);
        env.define(sym("proxy"), proxy);
        assertEquals("result", eval(read("(proxy (apply \"abc\" 123))"), env));
        
    }

}

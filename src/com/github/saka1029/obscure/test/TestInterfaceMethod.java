package com.github.saka1029.obscure.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.BeforeClass;

import static com.github.saka1029.obscure.core.Global.*;

import org.junit.Test;

import com.github.saka1029.obscure.core.Env;
import com.github.saka1029.obscure.core.Procedure;

public class TestInterfaceMethod {

    Env env = Env.create();
    
    @BeforeClass
    public static void before() {
        // Setインタフェースにメソッドを追加する。
        defineClassEnv(Set.class, "has", (Procedure)(self, args) -> ((Set<?>)self).contains(car(args)));
        defineGlobalEnv("HashSet", HashSet.class);
    }
    
    @Test
    public void test() {
        assertEquals(new HashSet<Integer>(), eval(read("(define s (HashSet (new)))"), env));
        assertEquals(true, eval(read("(s (add 1))"), env));
        assertEquals(new HashSet<Integer>(Arrays.asList(1)), eval(read("s"), env));
        assertEquals(true, eval(read("(s (has 1))"), env));
        assertEquals(false, eval(read("(s (has 2))"), env));
    }

}

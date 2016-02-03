package com.github.saka1029.obscure.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.github.saka1029.obscure.core.Env;
import com.github.saka1029.obscure.core.Pair;

import static com.github.saka1029.obscure.core.Global.*;

public class TestLet {

    static Env env = Env.create();
   
    @Test
    public void testLet() {
//        try (Debug a = new Debug("testLet()")) {
            assertEquals(3, eval(read("(let ((x 1) (y 2)) (+ x y))"), env));
            assertEquals(Pair.of(1, 2), eval(read("(let ((x 1) (y 2)) (cons x y))"), env));
//        }
    }

}

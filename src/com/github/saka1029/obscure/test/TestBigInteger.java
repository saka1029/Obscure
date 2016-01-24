package com.github.saka1029.obscure.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.math.BigInteger;

import org.junit.BeforeClass;
import org.junit.Test;

import com.github.saka1029.obscure.core.Env;
import com.github.saka1029.obscure.core.Procedure;

import static com.github.saka1029.obscure.core.Global.*;

public class TestBigInteger {

    static Env env = Env.create();
    
    @BeforeClass
    public static void before() throws IOException {
        defineClass(BigInteger.class, "<", (Procedure)(self, args) -> ((BigInteger)self).compareTo((BigInteger)car(args)) < 0);
        defineClass(BigInteger.class, "+", (Procedure)(self, args) -> ((BigInteger)self).add((BigInteger)car(args)));
        defineClass(BigInteger.class, "-", (Procedure)(self, args) -> ((BigInteger)self).subtract((BigInteger)car(args)));
        defineClass(BigInteger.class, "*", (Procedure)(self, args) -> ((BigInteger)self).multiply((BigInteger)car(args)));
    }

    static final BigInteger FACT100 = new BigInteger(
          "93326215443944152681699238856266700490715968"
        + "26438162146859296389521759999322991560894146"
        + "39761565182862536979208272237582511852109168"
        + "64000000000000000000000000");

    @Test
    public void testFact() throws IOException {
        eval(read(
            "(define (fact n)"
            + "  (if (< n 1I)"
            + "      1I"
            + "      (* (fact (- n 1I)) n)))"), env);
        assertEquals(FACT100, eval(read("(fact 100I)"), env));
    }
    
}

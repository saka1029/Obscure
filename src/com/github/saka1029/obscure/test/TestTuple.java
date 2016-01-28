package com.github.saka1029.obscure.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.github.saka1029.obscure.core.Tuple;
import com.github.saka1029.obscure.core.Tuple2;
import com.github.saka1029.obscure.core.Tuple3;

public class TestTuple {

    @Test
    public void testTuple() {
        Tuple2<String, Integer> t20 = Tuple.of("abc", 123);
        Tuple2<String, Integer> t21 = Tuple.of("a" + "bc", 100 + 23);
        Tuple3<String, Integer, Double> t30 = Tuple.of("abc", 123, 789.123);
        assertEquals(t20, t21);
        assertEquals("abc", t21.get0());
        assertEquals(123, (int)t21.get1());
        assertEquals(t20.hashCode(), t21.hashCode());
        assertNotEquals(t20, t30);
        assertNotEquals(t20.hashCode(), t30.hashCode());
        assertEquals("Tuple[abc, 123, 789.123]", t30.toString());
    }

}

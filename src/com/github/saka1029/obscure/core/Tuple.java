package com.github.saka1029.obscure.core;

import java.util.Arrays;

public class Tuple {

    private final Object[] values;
 
    Tuple(Object... values) {
        this.values = Arrays.copyOf(values, values.length);
    }

    Object get(int index) {
        return values[index];
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(values);
    }
 
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Tuple))
            return false;
        return Arrays.equals(values, ((Tuple)obj).values);
    }
    
    @Override
    public String toString() {
        return "Tuple" + Arrays.toString(values);
    }
  
    public static <A, B> Tuple2<A, B> of(A a, B b) { return new Tuple2<>(a,b); }
    public static <A, B, C> Tuple3<A, B, C> of(A a, B b, C c) { return new Tuple3<>(a,b, c); }
    public static <A, B, C, D> Tuple4<A, B, C, D> of(A a, B b, C c, D d) { return new Tuple4<>(a, b, c, d); }
    public static <A, B, C, D, E> Tuple5<A, B, C, D, E> of(A a, B b, C c, D d, E e) { return new Tuple5<>(a, b, c, d, e); }
}

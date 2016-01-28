package com.github.saka1029.obscure.core;

public class Tuple4<A, B, C, D> {
    
    private final Tuple tuple;
    
    Tuple4(A a, B b, C c, D d) { tuple = new Tuple(a, b, c, d); }

    @SuppressWarnings("unchecked") public A get0() { return (A)tuple.get(0); }
    @SuppressWarnings("unchecked") public B get1() { return (B)tuple.get(1); }
    @SuppressWarnings("unchecked") public C get2() { return (C)tuple.get(2); }
    @SuppressWarnings("unchecked") public D get3() { return (D)tuple.get(3); }
    
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Tuple4))
            return false;
        return tuple.equals(((Tuple4<?, ?, ?, ?>)obj).tuple);
    }
    
    @Override
    public int hashCode() {
        return tuple.hashCode();
    }
    
    @Override
    public String toString() {
        return tuple.toString();
    }
}
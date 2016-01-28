package com.github.saka1029.obscure.core;

public class Tuple3<A, B, C> {
    
    private final Tuple tuple;
    
    Tuple3(A a, B b, C c) { tuple = new Tuple(a, b, c); }

    @SuppressWarnings("unchecked") public A get0() { return (A)tuple.get(0); }
    @SuppressWarnings("unchecked") public B get1() { return (B)tuple.get(1); }
    @SuppressWarnings("unchecked") public C get2() { return (C)tuple.get(2); }
    
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Tuple3))
            return false;
        return tuple.equals(((Tuple3<?, ?, ?>)obj).tuple);
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

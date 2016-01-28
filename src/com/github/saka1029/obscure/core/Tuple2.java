package com.github.saka1029.obscure.core;

public class Tuple2<A, B> {
    
    private final Tuple tuple;
    
    Tuple2(A a, B b) { tuple = new Tuple(a, b); }

    @SuppressWarnings("unchecked") public A get0() { return (A)tuple.get(0); }
    @SuppressWarnings("unchecked") public B get1() { return (B)tuple.get(1); }
    
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Tuple2))
            return false;
        return tuple.equals(((Tuple2<?, ?>)obj).tuple);
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

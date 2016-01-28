package com.github.saka1029.obscure.core;

public class Tuple5<A, B, C, D, E> {
    
    private final Tuple tuple;
    
    Tuple5(A a, B b, C c, D d, E e) { tuple = new Tuple(a, b, c, d, e); }

    @SuppressWarnings("unchecked") public A get0() { return (A)tuple.get(0); }
    @SuppressWarnings("unchecked") public B get1() { return (B)tuple.get(1); }
    @SuppressWarnings("unchecked") public C get2() { return (C)tuple.get(2); }
    @SuppressWarnings("unchecked") public D get3() { return (D)tuple.get(3); }
    @SuppressWarnings("unchecked") public E get4() { return (E)tuple.get(4); }
    
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Tuple5))
            return false;
        return tuple.equals(((Tuple5<?, ?, ?, ?, ?>)obj).tuple);
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
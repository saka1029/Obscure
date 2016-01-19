package com.github.saka1029.obscure.core;

public class Nil extends List {

    public static Nil VALUE = new Nil();
    
    private Nil() {}

    @Override
    public Object car() {
        throw new ObscureException("cannot car of ()");
    }

    @Override
    public Object cdr() {
        throw new ObscureException("cannot cdr of ()");
    }
    
    @Override
    public String toString() {
        return "()";
    }

    @Override
    public Object eval(Env env) {
        return this;
    }

    @Override
    public Object invoke(Object self, Env env) {
        return this;
    }
}

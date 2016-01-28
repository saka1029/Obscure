package com.github.saka1029.obscure.core;

public interface Env {

    Object get(Symbol key);
    Object set(Symbol key, Object value);
    Object define(Symbol key, Object value);
    
    public static Env create(Env parent) {
        return new MapEnv(parent);
    }
    
    public static Env create() {
        return new MapEnv(Global.ENV);
    }
    
    public static final Env EMPTY = new MapEnv(null);
    
}

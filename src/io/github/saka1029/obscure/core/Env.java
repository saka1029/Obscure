package io.github.saka1029.obscure.core;

public interface Env {

    Object get(Symbol key);
    Object define(Symbol key, Object value);
    Object set(Symbol key, Object value);
    
}

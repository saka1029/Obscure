package com.github.saka1029.obscure.core;

import java.util.HashMap;
import java.util.Map;

public class MapEnv implements Env {

    private final Map<Symbol, Object> map = new HashMap<>();
    private final Env parent;
    
    MapEnv(Env parent) {
        this.parent = parent;
    }
    
    @Override
    public Object get(Symbol key) {
        Object value = map.get(key);
        if (value == null && parent != null)
            value = parent.get(key);
        return value;
    }

    @Override
    public Object define(Symbol key, Object value) {
        map.put(key, value);
        return value;
    }

    @Override
    public Object set(Symbol key, Object value) {
        if (map.containsKey(key)) {
            map.put(key, value);
            return value;
        } else if (parent != null)
            return parent.set(key, value);
        else
            throw new ObscureException("symbol '%s' not defined", key);
    }
    
}

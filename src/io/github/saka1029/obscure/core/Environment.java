package io.github.saka1029.obscure.core;

import java.util.HashMap;
import java.util.Map;

public interface Environment {

    Object get(Symbol key);
    Object define(Symbol key, Object value);
    Object set(Symbol key, Object value);
    
    public static Environment create() {
        return new MapEnvironment(Obscure.GLOBAL);
    }

    public static Environment create(Environment parent) {
        return new MapEnvironment(parent);
    }

}

class MapEnvironment implements Environment {

    private final Map<Symbol, Object> map = new HashMap<>();
    private final Environment parent;
    
    MapEnvironment(Environment parent) {
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
            throw new ObscureException("cannot set %s = %s", key, value);
    }
    
}
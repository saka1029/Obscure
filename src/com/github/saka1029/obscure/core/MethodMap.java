package com.github.saka1029.obscure.core;

import java.util.HashMap;
import java.util.Map;

public class MethodMap {

    private final Map<Symbol, Map<Class<?>, Applicable>> map = new HashMap<>();
 
    public Applicable get(Class<?> cls, Symbol method) {
        Map<Class<?>, Applicable> sub = map.get(method);
        if (sub == null)
            return null;
        return sub.get(cls);
    }
    
    public void define(Class<?> cls, Symbol method, Applicable applicable) {
        Map<Class<?>, Applicable> sub =
            map.computeIfAbsent(method, k -> new HashMap<>());
        sub.put(cls, applicable);
    }
}

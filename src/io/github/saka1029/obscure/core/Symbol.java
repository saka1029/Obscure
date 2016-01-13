package io.github.saka1029.obscure.core;

import java.util.HashMap;
import java.util.Map;

public class Symbol {

    private static final Map<String, Symbol> map = new HashMap<>();
    public static final Symbol QUOTE = of("quote");
 
    final String name;
 
    private Symbol(String name) {
        this.name = name;
        map.put(name, this);
    }
 
    public static Symbol of(String name) {
        Symbol r = map.get(name);
        if (r == null)
            r = new Symbol(name);
        return r;
    }

    @Override
    public String toString() {
        return name;
    }
}

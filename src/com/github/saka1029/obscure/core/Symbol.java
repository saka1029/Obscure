package com.github.saka1029.obscure.core;

import java.util.HashMap;
import java.util.Map;

public class Symbol implements Evalable, Invokable {

    static final Map<String, Symbol> map = new HashMap<>();
    static final Symbol QUOTE = new Symbol("quote");
    
    final String name;
    
    private Symbol(String name) {
        this.name = name;
        map.put(name, this);
    }

    @Override
    public Object eval(Env env) {
        return env.get(this);
    }
    
    @Override
    public Object invoke(Object self, Env env) {
        Object value = Global.getClassEnv(self.getClass(), this);
        if (value == null)
            value = Reflection.field(self, name);
        if (value == Reflection.NOT_FOUND)
            throw new ObscureException("%s does not have field %s", self, name);
        return value;
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

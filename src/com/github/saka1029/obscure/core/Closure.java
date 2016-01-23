package com.github.saka1029.obscure.core;

import static com.github.saka1029.obscure.core.Global.*;

import java.util.function.Supplier;

public class Closure implements Procedure, Supplier<Object> {
 
    final List parms;
    final List body;
    final Env env;

    protected Closure(List parms, List body, Env env) {
        this.parms = parms;
        this.body = body;
        this.env = env;
    }
    
    public static Closure of(List parms, List body, Env env) {
        switch (parms.size()) {
            case 1: return new Closure1(parms, body, env);
            case 2: return new Closure2(parms, body, env);
            default: return new Closure(parms, body, env);
        }
    }
    
    static Env pairlis(List parms, List args, Env env) {
        Env n = Env.create(env);
        Object a = args;
        for (Object p : parms) {
            n.define((Symbol)p, car(a));
            a = cdr(a);
        }
        return n;
    }

    @Override
    public Object apply(Object self, List args) {
        Object r = null;
        Env n = pairlis(parms, args, env);
        for (Object e : body)
            r = eval(e, n);
        return r;
    }
    
    @Override
    public String toString() {
        return String.format("(closure %s %s)", print(parms), print(body));
    }

    @Override
    public Object get() {
        return apply(null, list());
    }

}
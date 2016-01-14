package io.github.saka1029.obscure.core;

import static io.github.saka1029.obscure.core.Obscure.*;

import java.util.List;
import java.util.function.Supplier;

public class Closure implements Procedure, Supplier<Object> {
 
    final List<Object> parms;
    final List<Object> body;
    final Env env;

    protected Closure(List<Object> parms, List<Object> body, Env env) {
        this.parms = parms;
        this.body = body;
        this.env = env;
    }
    
    public static Closure of(List<Object> parms, List<Object> body, Env env) {
        switch (parms.size()) {
            case 1: return new Closure1(parms, body, env);
            case 2: return new Closure2(parms, body, env);
            default: return new Closure(parms, body, env);
        }
    }
    
    static Env pairlis(List<Object> parms, List<Object> args, Env env) {
        Env n = env(env);
        for (int i = 0, size = parms.size(); i < size; ++i)
            n.define(asSymbol(parms.get(i)), args.get(i));
        return n;
    }

    @Override
    public Object apply(Object self, List<Object> args) {
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
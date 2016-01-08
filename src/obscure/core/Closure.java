package obscure.core;

import static obscure.core.Helper.*;

import java.util.function.Supplier;

public class Closure implements Procedure, Supplier<Object> {
 
    final Object parms;
    final List body;
    final Env env;

    protected Closure(Object parms, List body, Env env) {
        this.parms = parms;
        this.body = body;
        this.env = env;
    }
    
    static int parmsCount(Object parms) {
        if (isList(parms)) {
            int sum = 0;
            for (@SuppressWarnings("unused") Object e : asList(parms))
                ++sum;
            return sum;
        }
        return -1;
    }

    public static Closure of(Object parms, List body, Env env) {
        switch (parmsCount(parms)) {
            case 1: return new Closure1(parms, body, env);
            case 2: return new Closure2(parms, body, env);
            default: return new Closure(parms, body, env);
        }
    }
    
    static Env pairlis(Object parms, List args, Env env) {
        Env n = Env.create(env);
        Object p = parms;
        Object a = args;
        for (; isPair(p); p = cdr(p), a = cdr(a))
            n.define((Symbol)car(p), car(a));
        if (p instanceof Symbol)
            n.define((Symbol)p, a);
        return n;
    }

    @Override
    public Object apply(Object self, List args) {
        Object r = false;
        Env n = pairlis(parms, args, env);
        for (Object e : body)
            r = eval(e, n);
        return r;
    }
    
    @Override
    public String toString() {
        return print(cons(sym("Closure"), cons(parms, body)));
    }

    /**
     * method for Supplier<Object>
     */
    @Override
    public Object get() {
        return apply(null, Nil.value);
    }

}
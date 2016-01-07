package obscure.core;

import static obscure.core.ListHelper.*;

public class Closure implements Procedure {
 
    final Object parms;
    final List body;
    final Env env;

    public Closure(Object parms, List body, Env env) {
        this.parms = parms;
        this.body = body;
        this.env = env;
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

}
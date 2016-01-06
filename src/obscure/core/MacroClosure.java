package obscure.core;

import static obscure.core.ListHelper.*;

public class MacroClosure implements Macro {
 
    final Object parms;
    final List body;

    public MacroClosure(Object parms, List body) {
        this.parms = parms;
        this.body = body;
    }
    
    static Env pairlis(Object parms, List args, Env env) {
        Env n = Env.create(env);
        Object p = parms;
        Object a = args;
        for (; isPair(p); p = cdr(p), a = cdr(args))
            n.define((Symbol)car(p), car(a));
        if (p instanceof Symbol)
            n.define((Symbol)p, a);
        return n;
    }

    @Override
    public Object expand(List args, Env env) {
        Object r = false;
        Env n = pairlis(parms, args, env);
        for (Object e : body)
            r = Global.eval(e, n);
        return r;
    }

}
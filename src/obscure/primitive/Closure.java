package obscure.primitive;

import obscure.core.Env;
import obscure.core.Nil;
import obscure.core.Obj;
import obscure.core.ObscureException;
import obscure.core.Procedure;
import obscure.core.Symbol;

public class Closure extends Procedure {
 
    final Obj parms;
    final Obj body;
    final Env env;

    public Closure(Obj parms, Obj body, Env env) {
        check(parms);
        this.parms = parms;
        this.body = body;
        this.env = env;
    }
    
    static void check(Obj parms) {
        Obj p = parms;
        for (; p.isPair(); p = p.cdr())
            if (!p.car().isSymbol())
                throw new ObscureException("parameters must be symbol: %s", parms);
        if (p != Nil.value && !p.isSymbol())
            throw new ObscureException("parameters must be symbol: %s", parms);
    }

    Env pairlis(Obj args, Env env) {
        Obj p = parms;
        Obj a = args;
        Env n = Env.create(env);
        for (; p.isPair(); p = p.cdr(), a = a.cdr())
            n.define((Symbol)p.car(), a.car());
        if (p.isSymbol())
            n.define((Symbol)p, a);
        return n;
    }

    @Override
    public Obj apply(Obj args) {
        Obj b = body;
        Obj r = Nil.value;
        Env n = pairlis(args, env);
        for (; b.isPair(); b = b.cdr())
            r = b.car().eval(n);
        return r;
    }

}

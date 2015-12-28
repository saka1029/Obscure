package obscure.primitive;

import obscure.core.Env;
import obscure.core.Nil;
import obscure.core.Obj;
import obscure.core.Procedure;

public class Closure extends Procedure {
 
    public final Obj parms;
    final Obj body;
    final Env env;

    public Closure(Obj parms, Obj body, Env env) {
        check(parms);
        this.parms = parms;
        this.body = body;
        this.env = env;
    }
    
    @Override
    public Obj apply(Obj args) {
        Obj b = body;
        Obj r = Nil.value;
        Env n = pairlis(parms, args, env);
        for (; b.isPair(); b = b.cdr())
            r = b.car().eval(n);
        return r;
    }

}

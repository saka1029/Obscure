package obscure.syntax;

import obscure.core.Env;
import obscure.core.Nil;
import obscure.core.Obj;
import obscure.core.Syntax;

public class Instance extends Syntax {
    
    private final Env env;
    
    public Instance(Env env) {
        this.env = env;
    }
    
    @Override
    public Obj apply(Obj args, Env env) {
        Obj a = args;
        Env n = Env.coumpound(this.env, env);
        Obj r = Nil.value;
        for (; a.isPair(); a = a.cdr())
            r = a.car().eval(n);
        return r;
    }
}

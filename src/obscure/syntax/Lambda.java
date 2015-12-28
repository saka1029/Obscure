package obscure.syntax;

import obscure.core.Env;
import obscure.core.Obj;
import obscure.core.Applicable;
import obscure.primitive.Closure;

public class Lambda extends Applicable {
    
    @Override
    public Obj apply(Obj args, Env env) {
        return new Closure(args.car(), args.cdr(), env);
    }

}

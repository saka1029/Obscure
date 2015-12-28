package obscure.syntax;

import obscure.core.Env;
import obscure.core.Obj;
import obscure.core.Syntax;
import obscure.primitive.Closure;

public class Lambda extends Syntax {
    
    @Override
    public Obj apply(Obj args, Env env) {
        return new Closure(args.car(), args.cdr(), env);
    }

}

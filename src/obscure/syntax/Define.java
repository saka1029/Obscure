package obscure.syntax;

import obscure.core.Env;
import obscure.core.Obj;
import obscure.core.Symbol;
import obscure.core.Applicable;
import obscure.primitive.Closure;

public class Define extends Applicable {

    @Override
    public Obj apply(Obj args, Env env) {
        if (args.car().isSymbol())
            return env.define((Symbol)args.car(), args.cdr().car().eval(env));
        else
            return env.define((Symbol)args.car().car(),
                new Closure(args.car().cdr(), args.cdr(),
                env));
    }
}

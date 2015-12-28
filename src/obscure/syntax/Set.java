package obscure.syntax;

import obscure.core.Env;
import obscure.core.Obj;
import obscure.core.ObscureException;
import obscure.core.Symbol;
import obscure.core.Applicable;

public class Set extends Applicable {

    @Override
    public Obj apply(Obj args, Env env) {
        if (args.car().isSymbol())
            return env.replace((Symbol)args.car(), args.cdr().car().eval(env));
        throw new ObscureException("set: first argument must be symbol %s", args);
    }
}

package obscure.globals;

import obscure.core.Applicable;
import obscure.core.Closure;
import obscure.core.Env;
import obscure.core.Global;
import obscure.core.List;
import obscure.core.ObscureException;
import obscure.core.Pair;
import obscure.core.Symbol;

import static obscure.core.ListHelper.*;

public class Define implements Applicable {

    @Override
    public Object apply(Object self, List args, Env env) {
        Object first = car(args);
        if (first instanceof Symbol)
            return env.define((Symbol)first, Global.eval(cadr(args), env));
        else if (first instanceof Pair)
            return env.define((Symbol)car(first), new Closure(cdr(first), (List)cdr(args), env));
        else
            throw new ObscureException("cannot define %s", args);
    }

}

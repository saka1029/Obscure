package obscure.globals;

import obscure.core.Closure;
import obscure.core.Env;
import obscure.core.List;
import obscure.core.ObscureException;
import obscure.core.Pair;
import obscure.core.Symbol;

import static obscure.core.ListHelper.*;

import obscure.core.Applicable;

public class Define implements Applicable {

    @Override
    public Object apply(Object self, List args, Env env) {
        Object first = car(args);
        if (first instanceof Symbol)
            return env.define((Symbol)first, eval(cadr(args), env));
        else if (first instanceof Pair)
            return env.define((Symbol)car(first), Closure.of(cdr(first), (List)cdr(args), env));
        else
            throw new ObscureException("cannot define %s", args);
    }

}

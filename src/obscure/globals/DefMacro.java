package obscure.globals;

import obscure.core.Env;
import obscure.core.List;
import obscure.core.MacroClosure;
import obscure.core.ObscureException;
import obscure.core.Pair;
import obscure.core.Symbol;

import static obscure.core.ListHelper.*;

import obscure.core.Applicable;

public class DefMacro implements Applicable {

    @Override
    public Object apply(Object self, List args, Env env) {
        Object first = car(args);
        if (first instanceof Symbol)
            throw new ObscureException("first argument must be list but %s", first);
        else if (first instanceof Pair)
            return env.define((Symbol)car(first), new MacroClosure(cdr(first), (List)cdr(args)));
        else
            throw new ObscureException("cannot define %s", args);
    }

}

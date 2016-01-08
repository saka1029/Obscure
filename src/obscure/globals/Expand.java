package obscure.globals;

import obscure.core.Applicable;
import obscure.core.Env;
import obscure.core.List;
import obscure.core.Macro;

import static obscure.core.Helper.*;

public class Expand implements Applicable {

    @Override
    public Object apply(Object self, List args, Env env) {
        Object arg = args.car();
        if (!isPair(arg))
            return arg;
        Object first = car(arg);
        Object proc = eval(first, env);
        if (proc instanceof Macro)
            return ((Macro)proc).expand((List)cdr(arg), env);
        return arg;
    }

}

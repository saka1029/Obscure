package obscure.globals;

import obscure.core.Closure;
import obscure.core.Env;
import obscure.core.List;
import static obscure.core.ListHelper.*;

import obscure.core.Applicable;

public class Lambda implements Applicable {

    @Override
    public Object apply(Object self, List args, Env env) {
        return new Closure(car(args), (List)cdr(args), env);
    }

}

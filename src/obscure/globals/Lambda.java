package obscure.globals;

import obscure.core.Applicable;
import obscure.core.Closure;
import obscure.core.Env;
import obscure.core.List;
import static obscure.core.ListHelper.*;

public class Lambda implements Applicable {

    @Override
    public Object apply(Object self, List args, Env env) {
        return new Closure(car(args), (List)cdr(args), env);
    }

}

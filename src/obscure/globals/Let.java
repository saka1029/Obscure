package obscure.globals;

import obscure.core.Env;
import obscure.core.List;
import static obscure.core.ListHelper.*;
import obscure.core.Macro;
import obscure.core.Pair;
import obscure.core.Symbol;

public class Let implements Macro {

    @Override
    public Object expand(List args, Env env) {
        Object body = cdr(args);
        List decl = asList(car(args));
        Pair.Builder p = new Pair.Builder();
        Pair.Builder a = new Pair.Builder();
        for (Object e : decl) {
            p.tail(car(e));
            a.tail(cadr(e));
        }
        return cons(cons(Symbol.LAMBDA, cons(p.build(), body)), a.build());
    }

}

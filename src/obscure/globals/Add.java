package obscure.globals;

import obscure.core.Env;
import obscure.core.List;
import static obscure.core.ListHelper.*;
import obscure.core.Macro;
import obscure.core.Symbol;

public class Add implements Macro {

    @Override
    public Object expand(List args, Env env) {
        return cons(car(args), cons(Symbol.of("+"), cdr(args)));
    }

}

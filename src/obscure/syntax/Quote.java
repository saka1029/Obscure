package obscure.syntax;

import obscure.core.Env;
import obscure.core.Obj;
import obscure.core.Syntax;

public class Quote extends Syntax {

    @Override
    public Obj apply(Obj args, Env env) {
        return args.car();
    }
}

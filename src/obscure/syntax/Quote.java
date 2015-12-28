package obscure.syntax;

import obscure.core.Env;
import obscure.core.Obj;
import obscure.core.Applicable;

public class Quote extends Applicable {

    @Override
    public Obj apply(Obj args, Env env) {
        return args.car();
    }
}

package obscure.syntax;

import obscure.core.Applicable;
import obscure.core.Env;
import obscure.core.Obj;

public class New extends Applicable {

    @Override
    public Obj apply(Obj args, Env env) {
        return new Instance(env);
    }

}

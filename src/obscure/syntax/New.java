package obscure.syntax;

import obscure.core.Env;
import obscure.core.Obj;
import obscure.core.Syntax;

public class New extends Syntax {

    @Override
    public Obj apply(Obj args, Env env) {
        return new Instance(env);
    }
}

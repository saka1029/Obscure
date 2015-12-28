package obscure.core;

public abstract class Procedure extends Applicable {


    public abstract Obj apply(Obj args);

    @Override
    public Obj apply(Obj args, Env env) {
        return apply(evlis(args, env));
    }


}

package obscure.core;

public abstract class Procedure extends Obj {

    Obj evlis(Obj args, Env env) {
        Pair.Builder b = Pair.builder();
        for (; args.isPair(); args = args.cdr())
            b.tail(args.car().eval(env));
        return b.build();
    }

    public abstract Obj apply(Obj args);

    @Override
    public Obj apply(Obj args, Env env) {
        return apply(evlis(args, env));
    }
}

package obscure.core;

public abstract class Applicable extends Obj {

    public static Obj evlis(Obj args, Env env) {
        Pair.Builder b = Pair.builder();
        for (; args.isPair(); args = args.cdr())
            b.tail(args.car().eval(env));
        return b.build();
    }

    public static void check(Obj parms) {
        Obj p = parms;
        for (; p.isPair(); p = p.cdr())
            if (!p.car().isSymbol())
                throw new ObscureException("parameters must be symbol: %s", parms);
        if (p != Nil.value && !p.isSymbol())
            throw new ObscureException("parameters must be symbol: %s", parms);
    }

    public static Env pairlis(Obj parms, Obj args, Env env) {
        Obj p = parms;
        Obj a = args;
        Env n = Env.create(env);
        for (; p.isPair(); p = p.cdr(), a = a.cdr())
            n.define((Symbol)p.car(), a.car());
        if (p.isSymbol())
            n.define((Symbol)p, a);
        return n;
    }

}

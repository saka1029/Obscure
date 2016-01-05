package obscure.core;

public interface Procedure extends Applicable {

    Object apply(Object self, List args);

    static List evlis(List args, Env env) {
        Pair.Builder b = new Pair.Builder();
        for (List a = args; a.isPair(); a = (List)a.cdr())
            b.tail(Global.eval(a.car(), env));
        return b.build();
    }

    @Override
    public default Object apply(Object self, List args, Env env) {
        return apply(self, evlis(args, env));
    }

}

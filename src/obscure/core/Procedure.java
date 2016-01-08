package obscure.core;

import static obscure.core.Helper.*;

public interface Procedure extends Applicable {

    Object apply(Object self, List args);

    static List evlis(List args, Env env) {
        Pair.Builder b = new Pair.Builder();
        for (Object e : args)
            b.tail(eval(e, env));
        return b.build();
    }

    @Override
    public default Object apply(Object self, List args, Env env) {
        return apply(self, evlis(args, env));
    }

}

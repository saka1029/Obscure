package com.github.saka1029.obscure.core;

public interface Procedure extends Applicable {

    Object apply(Object self, List args);

    public static List evlis(List args, Env env) {
        Pair.Builder b = Pair.builder();
        for (Object e : args)
            b.tail(Global.eval(e, env));
        return b.build();
    }

    @Override
    public default Object apply(Object self, List args, Env env) {
        return apply(self, evlis(args, env));
    }

}

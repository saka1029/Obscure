package com.github.saka1029.obscure.core;

import static com.github.saka1029.obscure.core.Global.*;

public interface Macro extends Applicable {
 
    Object expand(List args);
 
    @Override
    default Object apply(Object self, List args, Env env) {
        Object expanded = expand(args);
        if (DEBUG)
            System.out.printf("%sexpanded: %s -> %s%n", indent(INDENT), args, expanded);
        return Global.eval(expanded, env);
    }

}

package com.github.saka1029.obscure.core;

public interface Macro extends Applicable {
 
    Object expand(List args);
 
    @Override
    default Object apply(Object self, List args, Env env) {
        Object expanded = expand(args);
        System.out.printf("expanded: %s -> %s%n", args, expanded);
        return Global.eval(expanded, env);
    }

}

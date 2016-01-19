package com.github.saka1029.obscure.core;

public interface Applicable {

    Object apply(Object self, List args, Env env);
}

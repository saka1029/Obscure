package io.github.saka1029.obscure.core;

import static io.github.saka1029.obscure.core.Obscure.*;

import java.util.List;

public class Define implements Applicable {

    @Override
    public Object apply(Object self, List<Object> args, Env env) {
        Object first = car(args);
        if (isSymbol(first))
            return env.define(asSymbol(first), eval(cadr(args), env));
        else if (isList(first))
            return env.define(asSymbol(car(first)), Closure.of(cdr(first), asList(cdr(args)), env));
        else
            throw new ObscureException("cannot define %s", args);
    }

}

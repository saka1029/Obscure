package com.github.saka1029.obscure.core;

import static com.github.saka1029.obscure.core.Global.*;

public class GenericOperator implements Macro {

    final Symbol op;
    
    public GenericOperator(Symbol op) {
        this.op = op;
    }

    @Override
    public Object expand(List args) {
        Pair.Builder r = Pair.builder();
        r.tail(car(args));
        for (Object e = cdr(args); e instanceof Pair; e = cdr(e))
            r.tail(list(op, car(e)));
        return r.build();
    }

}

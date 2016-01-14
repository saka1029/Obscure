package io.github.saka1029.obscure.core;

import java.util.ArrayList;
import java.util.List;

import static io.github.saka1029.obscure.core.Obscure.*;

public class GenericOperator implements Macro {

    final Symbol op;
    
    public GenericOperator(Symbol op) {
        this.op = op;
    }

    @Override
    public Object expand(List<Object> args, Env env) {
        List<Object> r = new ArrayList<>();
        r.add(args.get(0));
        for (int i = 1, max = args.size(); i < max; ++i)
            r.add(list(op, args.get(i)));
        return r;
    }

}

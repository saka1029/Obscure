package io.github.saka1029.obscure.core;

import java.util.ArrayList;
import java.util.List;
import static io.github.saka1029.obscure.core.Obscure.*;

public class Let implements Macro {

    @Override
    public Object expand(List<Object> args, Env env) {
        List<Object> parms = new ArrayList<>();
        List<Object> actual = new ArrayList<>();
        for (Object e : asList(args.get(0))) {
            parms.add(asList(e).get(0));
            actual.add(asList(e).get(1));
        }
        return append(list(append(list(sym("lambda"), parms), cdr(args))), actual);
                
    }

}

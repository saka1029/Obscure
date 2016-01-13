package io.github.saka1029.obscure.core;

import static io.github.saka1029.obscure.core.Obscure.*;

import java.util.List;

public interface Macro extends Applicable {
 
    Object expand(List<Object> args, Environment env);
 
    @Override
    default Object apply(Object self, List<Object> args, Environment env) {
        Object expanded = expand(args, env);
        System.out.printf("expanded: %s -> %s%n", print(args), print(expanded));
        return eval(expanded, env);
    }

}

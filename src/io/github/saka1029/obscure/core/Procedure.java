package io.github.saka1029.obscure.core;

import static io.github.saka1029.obscure.core.Obscure.*;

import java.util.List;
import java.util.stream.Collectors;

public interface Procedure extends Applicable {

    Object apply(Object self, List<Object> args);

    static List<Object> evlis(List<Object> args, Environment env) {
        return args.stream()
            .map(e -> eval(e, env))
            .collect(Collectors.toList());
    }

    @Override
    public default Object apply(Object self, List<Object> args, Environment env) {
        return apply(self, evlis(args, env));
    }

}

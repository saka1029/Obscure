package obscure.core;

import static obscure.core.Helper.*;

public interface Macro extends Applicable {
    
    Object expand(List args, Env env);
    
    @Override
    default Object apply(Object self, List args, Env env) {
        Object expanded = expand(args, env);
        System.out.printf("expanded: %s -> %s%n", print(args), print(expanded));
        return eval(expanded, env);
    }

}

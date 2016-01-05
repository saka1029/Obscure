package obscure.wrappers;

import obscure.core.Env;
import obscure.core.List;
import obscure.core.Procedure;
import obscure.core.Symbol;

public class StringWrapper {

    public static final Env VALUE = Env.create(ObjectWrapper.VALUE);
    
    static List asList(Object o) {
        return (List)o;
    }

    static {
        VALUE.define(Symbol.of("+"), (Procedure) ((self, args) -> {
            StringBuilder sb = new StringBuilder();
            sb.append(self);
            for (List a = args; a.isPair(); a = asList(a.cdr()))
                sb.append(a.car());
            return sb.toString();
        }));
    }

}

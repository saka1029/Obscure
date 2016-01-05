package obscure.wrappers;

import obscure.core.Env;
import obscure.core.List;
import obscure.core.Procedure;
import obscure.core.Symbol;

public class IntegerWrapper {

    public static final Env VALUE = Env.create(ObjectWrapper.VALUE);
    
    static List asList(Object object) {
        return (List)object;
    }
    
    static {
        VALUE.define(Symbol.of("+"), (Procedure) ((self, args) -> {
            int sum = 0;
            sum += (int)self;
            for (List a = args; a.isPair(); a = asList(a.cdr()))
                sum += (int)a.car();
            return sum;
        }));
        VALUE.define(Symbol.of("*"), (Procedure) ((self, args) -> {
            int sum = 1;
            sum *= (int)self;
            for (List a = args; a.isPair(); a = asList(a.cdr()))
                sum *= (int)a.car();
            return sum;
        }));
    }

}

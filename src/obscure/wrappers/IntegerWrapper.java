package obscure.wrappers;

import obscure.core.Procedure;
import obscure.core.Symbol;

public class IntegerWrapper extends AbstractWrapper {
    
    @Override
    public Class<?> wrapClass() {
        return Integer.class;
    }

    @Override
    public Class<?> parentClass() {
        return Object.class;
    }

    public IntegerWrapper() {
        map.put(Symbol.of("+"), (Procedure) ((self, args) -> {
            int sum = 0;
            sum += (int)self;
            for (Object e : args)
                sum += (int)e;
            return sum;
        }));
        map.put(Symbol.of("*"), (Procedure) ((self, args) -> {
            int p = 1;
            p *= (int)self;
            for (Object e : args)
                p *= (int)e;
            return p;
        }));
    }

}

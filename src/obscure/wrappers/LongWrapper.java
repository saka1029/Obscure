package obscure.wrappers;

import obscure.core.Procedure;
import obscure.core.Symbol;

public class LongWrapper extends AbstractWrapper {
    
    @Override
    public Class<?> wrapClass() {
        return Long.class;
    }

    public LongWrapper() {
        map.put(Symbol.of("+"), (Procedure) ((self, args) -> {
            long sum = 0;
            sum += (long)self;
            for (Object e : args)
                sum += (long)e;
            return sum;
        }));
        map.put(Symbol.of("*"), (Procedure) ((self, args) -> {
            long p = 1;
            p *= (long)self;
            for (Object e : args)
                p *= (long)e;
            return p;
        }));
    }

}

package obscure.wrappers;

import static obscure.core.ListHelper.*;
import obscure.core.Pair;
import obscure.core.Procedure;
import obscure.core.Symbol;

public class PairWrapper extends AbstractWrapper {
    
    @Override
    public Class<?> wrapClass() {
        return Pair.class;
    }

    @Override
    public Class<?> parentClass() {
        return Object.class;
    }

    public PairWrapper() {
        map.put(Symbol.of("car"), (Procedure)(self, args) -> car(self));
        map.put(Symbol.of("cdr"), (Procedure)(self, args) -> cdr(self));
        map.put(Symbol.of("append"), new Append());
    }

}

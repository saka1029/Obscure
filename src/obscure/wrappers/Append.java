package obscure.wrappers;

import obscure.core.List;
import obscure.core.Pair;

import static obscure.core.Helper.*;
import obscure.core.Procedure;

public class Append implements Procedure {

    @Override
    public Object apply(Object self, List args) {
        Pair.Builder b = new Pair.Builder();
        for (Object e : asList(self))
            b.tail(e);
        for (Object e : args)
            for (Object f : asList(e))
                b.tail(f);
        return b.build();
    }

}

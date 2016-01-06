package obscure.globals;

import obscure.core.List;
import static obscure.core.ListHelper.*;
import obscure.core.Macro;
import obscure.core.Nil;

public class Cascade implements Macro {

    @Override
    public Object expand(List args) {
        Object w = Nil.value;
        for (Object e : args)
            w = w == Nil.value ? e : cons(car(e), cons(w, cdr(e)));
        return w;
    }

}

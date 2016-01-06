package obscure.globals;

import obscure.core.List;
import static obscure.core.ListHelper.*;
import obscure.core.Procedure;

public class Cons implements Procedure {

    @Override
    public Object apply(Object self, List args) {
        return cons(car(args), cadr(args));
    }

}

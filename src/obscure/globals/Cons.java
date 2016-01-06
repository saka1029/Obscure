package obscure.globals;

import obscure.core.List;
import obscure.core.Procedure;

import static obscure.core.ListHelper.*;

public class Cons implements Procedure {

    @Override
    public Object apply(Object self, List args) {
        return cons(car(args), cadr(args));
    }

}

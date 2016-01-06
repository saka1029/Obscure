package obscure.globals;

import obscure.core.Applicable;
import obscure.core.Env;
import obscure.core.List;
import static obscure.core.ListHelper.*;
import obscure.core.MacroClosure;

public class MakeMacro implements Applicable {

    @Override
    public Object apply(Object self, List args, Env env) {
        return new MacroClosure(car(args), asList(cdr(args)));
    }

}

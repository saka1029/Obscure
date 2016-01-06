package obscure.globals;

import obscure.core.Applicable;
import obscure.core.Env;
import obscure.core.List;
import static obscure.core.ListHelper.*;

public class Quote implements Applicable {

    @Override
    public Object apply(Object self, List args, Env env) {
        return car(args);
    }

}

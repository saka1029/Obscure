package obscure.wrappers;

import obscure.core.List;
import obscure.core.ObscureException;
import obscure.core.Procedure;
import obscure.core.Reflection;

public class ConstructorProcedure implements Procedure {

    @Override
    public Object apply(Object self, List args) {
        Object r = Reflection.constructor(self, args.toArray());
        if (r == Reflection.NOT_FOUND)
            throw new ObscureException(
                "no constructor for new %s%s", self, args);
        return r;
    }

}

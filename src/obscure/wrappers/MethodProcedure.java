package obscure.wrappers;

import obscure.core.List;
import obscure.core.ObscureException;
import obscure.core.Procedure;
import obscure.core.Reflection;

public class MethodProcedure implements Procedure {

    final String name;

    static final Object NOT_FOUND = new Object();
    
    MethodProcedure(String name) {
        this.name = name;
    }

    @Override
    public Object apply(Object self, List args) {
        Object result = Reflection.method(self, name, args.toArray());
        if (result == NOT_FOUND)
            throw new ObscureException(
                "no methods %s %s in %s", name, args, self.getClass());
        return result;
    }

}

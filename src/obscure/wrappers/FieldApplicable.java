package obscure.wrappers;

import obscure.core.Applicable;
import obscure.core.Env;
import obscure.core.List;
import obscure.core.ObscureException;
import obscure.core.Reflection;

public class FieldApplicable implements Applicable {

    final String name;
 
    public FieldApplicable(String name) {
        this.name = name;
    }
    
    @Override
    public Object apply(Object self, List args, Env env) {
        Object result = Reflection.field(self, name);
        if (result == Reflection.NOT_FOUND)
            throw new ObscureException("no fields %s in %s", name, self);
        return result;
    }

}

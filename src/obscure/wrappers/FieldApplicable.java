package obscure.wrappers;

import java.lang.reflect.Field;

import obscure.core.Applicable;
import obscure.core.Env;
import obscure.core.List;
import obscure.core.ObscureException;

public class FieldApplicable implements Applicable {

    final String name;
 
    public FieldApplicable(String name) {
        this.name = name;
    }
    
    static final Object NOT_FOUND = new Object();

    Object apply(Object self, Class<?> clas) {
        try {
            Field f = clas.getField(name);
            return f.get(self);
        } catch (NoSuchFieldException n) {
            return NOT_FOUND;
        } catch (SecurityException
                | IllegalArgumentException
                | IllegalAccessException e) {
            throw new ObscureException(e);
        }
    }

    @Override
    public Object apply(Object self, List args, Env env) {
        Object result = apply(self, self.getClass());
        if (result == NOT_FOUND && self instanceof Class)
            result = apply(self, (Class<?>)self);
        if (result == NOT_FOUND)
            throw new ObscureException("no fields %s in %s", name, self);
        return result;
    }

}

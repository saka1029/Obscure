package obscure.wrappers;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import obscure.core.List;
import obscure.core.ObscureException;

public class ConstructorProcedure extends ExecutableProcedure {

    ConstructorProcedure() {}
    
    @Override
    public Object apply(Object self, List args) {
        for (Constructor<?> c : ((Class<?>)self).getConstructors()) {
            Object[] actual = actualArguments(c, args);
            if (actual == null)
                continue;
            try {
                return c.newInstance(actual);
            } catch (InvocationTargetException i) {
                throw new ObscureException(i.getCause());
            } catch (InstantiationException
                    | IllegalAccessException
                    | IllegalArgumentException e) {
                throw new ObscureException(e);
            }
        }
        throw new ObscureException(
            "no constructor for new %s%s", self, args);
    }

}

package obscure.wrappers;

import java.lang.reflect.Method;

import obscure.core.List;
import obscure.core.ObscureException;

import java.lang.reflect.InvocationTargetException;

public class MethodProcedure extends ExecutableProcedure {

    final String name;

    static final Object NOT_FOUND = new Object();
    
    MethodProcedure(String name) {
        this.name = name;
    }

    private Object apply(Object self, List args, Class<?> clas) {
        for (Method m : clas.getMethods()) {
            if (!m.getName().equals(name))
                continue;
            Object[] actual = actualArguments(m, args);
            if (actual == null)
                continue;
            try {
                m.setAccessible(true);
                return m.invoke(self, actual);
            } catch (InvocationTargetException i) {
                throw new ObscureException(i.getCause());
            } catch (IllegalAccessException
                    | IllegalArgumentException e) {
                throw new ObscureException(e);
            }
        }
        return MethodProcedure.NOT_FOUND;
    }

    @Override
    public Object apply(Object self, List args) {
        Object result = apply(self, args, self.getClass());
        if (result == NOT_FOUND && self instanceof Class)
            result = apply(self, args, (Class<?>)self);
        if (result == NOT_FOUND)
            throw new ObscureException(
                "no methods %s %s in %s", name, args, self.getClass());
        return result;
    }

}

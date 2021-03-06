package com.github.saka1029.obscure.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import static com.github.saka1029.obscure.core.Global.*;

public class MethodProcedure implements Procedure {

    private final java.util.Set<Method> methods;
        
    public MethodProcedure(Set<Method> methods) {
        this.methods = methods;
    }

    @Override
    public Object apply(Object self, List args) {
        int length = args.size();
        Object[] allArgs = new Object[length];
        int i = 0;
        for (Object e : args)
            allArgs[i++] = e;
        for (Method e : methods) {
            Object[] actual = Reflection.actualArguments(e, allArgs);
            if (actual != null) {
                try {
                    e.setAccessible(true);
                    return e.invoke(self, actual);
                } catch (IllegalAccessException
                        | IllegalArgumentException
                        | InvocationTargetException ex) {
                    throw new ObscureException(ex);
                }
            }
        }
        throw new ObscureException("no method '%s' for arguments %s",
            methods.iterator().next().getName(),
            print(allArgs));
    }
}

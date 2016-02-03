package com.github.saka1029.obscure.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;

public class MethodProcedure implements Procedure {

    private final java.util.Set<Method> methods = new HashSet<>();
        
    void add(Method method) {
        methods.add(method);
    }

    @Override
    public Object apply(Object self, List args) {
        int length = args.size();
        Object[] allArgs = new Object[length + 1];
        allArgs[0] = self;
        int i = 1;
        for (Object e : args)
            allArgs[i++] = e;
        for (Method m : methods) {
            Object[] actual = Reflection.actualArguments(m, allArgs);
            if (actual != null) {
                try {
                    return m.invoke(null, actual);
                } catch (IllegalAccessException
                        | IllegalArgumentException
                        | InvocationTargetException e) {
                    throw new ObscureException(e);
                }
            }
        }
        throw new ObscureException("no match method for %s", Arrays.toString(allArgs));
    }
}

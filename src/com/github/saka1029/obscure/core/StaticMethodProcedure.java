package com.github.saka1029.obscure.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;

public class StaticMethodProcedure implements Procedure {

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
        for (Method e : methods) {
            Object[] actual = Reflection.actualArguments(e, allArgs);
            if (actual != null) {
                try {
                    e.setAccessible(true);
                    return e.invoke(null, actual);
                } catch (IllegalAccessException
                        | IllegalArgumentException
                        | InvocationTargetException ex) {
                    throw new ObscureException(ex);
                }
            }
        }
        throw new ObscureException("no match method for %s", Arrays.toString(allArgs));
    }
}

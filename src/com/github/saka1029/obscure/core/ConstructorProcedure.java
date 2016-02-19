package com.github.saka1029.obscure.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Set;

public class ConstructorProcedure implements Procedure {

    private final java.util.Set<Constructor<?>> constructors;
        
    public ConstructorProcedure(Set<Constructor<?>> constructors) {
        this.constructors = constructors;
    }

    @Override
    public Object apply(Object self, List args) {
        int length = args.size();
        Object[] allArgs = new Object[length];
        int i = 0;
        for (Object e : args)
            allArgs[i++] = e;
        for (Constructor<?> e : constructors) {
            Object[] actual = Reflection.actualArguments(e, allArgs);
            if (actual != null) {
                try {
                    e.setAccessible(true);
                    return e.newInstance(actual);
                } catch (IllegalAccessException
                        | IllegalArgumentException
                        | InstantiationException
                        | InvocationTargetException ex) {
                    throw new ObscureException(ex);
                }
            }
        }
        throw new ObscureException("no match constructor for %s", Arrays.toString(allArgs));
    }
}

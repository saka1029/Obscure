package com.github.saka1029.obscure.core;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

public class Reflection {

    private Reflection() {}
    
    public static final Object NOT_FOUND = new Object() {
        @Override public String toString() { return "NOT_FOUND"; };
    };

    private static Object field(Object self, String name, Class<?> clas) {
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

    public static Object field(Object self, String name) {
        Object result = field(self, name, self.getClass());
        if (result == NOT_FOUND && self instanceof Class)
            result = field(self, name, (Class<?>)self);
        return result;
    }

    private static final Map<Class<?>, Class<?>> PRIMITIVES = new HashMap<>();

    public static Class<?> getPrimitive(Class<?> cls) {
        return PRIMITIVES.get(cls);
    }
    
    private static void put(Class<?> primitive, Class<?> wrapper) {
        PRIMITIVES.put(primitive, wrapper);
        PRIMITIVES.put(wrapper, primitive);
    }

    static {
        put(Boolean.TYPE, Boolean.class);
        put(Byte.TYPE, Byte.class);
        put(Character.TYPE, Character.class);
        put(Short.TYPE, Short.class);
        put(Integer.TYPE, Integer.class);
        put(Long.TYPE, Long.class);
        put(Float.TYPE, Float.class);
        put(Double.TYPE, Double.class);
        put(Void.TYPE, Void.class);
    }

    private static boolean isAssignable(Class<?> parmType, Object arg) {
        if (parmType.isPrimitive()) {
            if (arg == null || !PRIMITIVES.get(parmType).isInstance(arg))
                return false;
        } else if (arg != null && !parmType.isInstance(arg))
            return false;
        return true;
    }

    private static Object[] actualArgumentsVarArgs(Executable e, Object... args) {
        Parameter[] parms = e.getParameters();
        int parmSize = parms.length;
        if (parmSize - 1 > args.length)
            return null;
        Object[] actual = new Object[parmSize];
        for (int i = 0; i < parmSize; ++i) {
            Parameter parm = parms[i];
            Class<?> type = parm.getType();
            // TODO: VarArgsとして配列が指定された場合はそのまま渡す。
            if (parm.isVarArgs()) {
                int varSize = args.length - i;
                if (varSize == 0 && type.isInstance(args[i]))
                    actual[i] = args[i];
                else {
                    Class<?> ctype = type.getComponentType();
                    Object vars = Array.newInstance(ctype, varSize);
                    for (int j = 0; j < varSize; ++j) {
                        Object arg = args[i + j];
                        if (!isAssignable(ctype, arg))
                            return null;
                        Array.set(vars, j, arg);
                    }
                    actual[i] = vars;
                }
            } else {
                Object arg = args[i];
                if (!isAssignable(type, arg))
                    return null;
                actual[i] = arg;
            }
        }
        return actual;
    }
    
    private static Object[] actualArgumentsNoVarArgs(Executable e, Object... args) {
        int argsSize = args.length;
        Object[] actual = new Object[argsSize];
        int parmSize = e.getParameterCount();
        if (parmSize != args.length)
            return null;
        Class<?>[] parmType = e.getParameterTypes();
        for (int i = 0; i < parmSize; ++i)
            if (!isAssignable(parmType[i], args[i]))
                return null;
            else
                actual[i] = args[i];
        return actual;
    }

    public static Object[] actualArguments(Executable e, Object... args) {
        if (e.isVarArgs())
            return actualArgumentsVarArgs(e, args);
        else
            return actualArgumentsNoVarArgs(e, args);
    }

    public static Object constructor(Object self, Object... args) {
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
        return NOT_FOUND;
    }

    public static Object method(Class<?> clas, Object self, String name, Object... args) {
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
        return NOT_FOUND;
    }

    public static Object method(Object self, String name, Object... args) {
        Object result = method(self.getClass(), self, name, args);
        if (result == NOT_FOUND && self instanceof Class)
            result = method((Class<?>)self, self, name, args);
        return result;
    }

}

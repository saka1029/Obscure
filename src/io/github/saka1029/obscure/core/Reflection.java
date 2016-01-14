package io.github.saka1029.obscure.core;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.List;
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

    private static Map<Class<?>, Class<?>> PRIMITIVES = new HashMap<>();
    
    static {
        PRIMITIVES.put(Boolean.TYPE, Boolean.class);
        PRIMITIVES.put(Byte.TYPE, Byte.class);
        PRIMITIVES.put(Character.TYPE, Character.class);
        PRIMITIVES.put(Short.TYPE, Short.class);
        PRIMITIVES.put(Integer.TYPE, Integer.class);
        PRIMITIVES.put(Long.TYPE, Long.class);
        PRIMITIVES.put(Float.TYPE, Float.class);
        PRIMITIVES.put(Double.TYPE, Double.class);
        PRIMITIVES.put(Void.TYPE, Void.class);
    }

    private static boolean isAssignable(Class<?> parmType, Object arg) {
        if (parmType.isPrimitive()) {
            if (arg == null || !PRIMITIVES.get(parmType).isInstance(arg))
                return false;
        } else if (arg != null && !parmType.isInstance(arg))
            return false;
        return true;
    }

    private static Object[] actualArgumentsVarArgs(Executable e, List<Object> args) {
        Parameter[] parms = e.getParameters();
        int parmSize = parms.length;
        if (parmSize - 1 > args.size())
            return null;
        Object[] actual = new Object[parmSize];
        for (int i = 0; i < parmSize; ++i) {
            Parameter parm = parms[i];
            Class<?> type = parm.getType();
            // TODO: VarArgsとして配列が指定された場合はそのまま渡す。
            if (parm.isVarArgs()) {
                int varSize = args.size() - i;
                if (varSize == 0 && type.isInstance(args.get(i)))
                    actual[i] = args.get(i);
                else {
                    Class<?> ctype = type.getComponentType();
                    Object vars = Array.newInstance(ctype, varSize);
                    for (int j = 0; j < varSize; ++j) {
                        Object arg = args.get(i + j);
                        if (!isAssignable(ctype, arg))
                            return null;
                        Array.set(vars, j, arg);
                    }
                    actual[i] = vars;
                }
            } else {
                Object arg = args.get(i);
                if (!isAssignable(type, arg))
                    return null;
                actual[i] = arg;
            }
        }
        return actual;
    }
    
    private static Object[] actualArgumentsNoVarArgs(Executable e, List<Object> args) {
        int argsSize = args.size();
        Object[] actual = new Object[argsSize];
        int parmSize = e.getParameterCount();
        if (parmSize != args.size())
            return null;
        Class<?>[] parmType = e.getParameterTypes();
        for (int i = 0; i < parmSize; ++i)
            if (!isAssignable(parmType[i], args.get(i)))
                return null;
            else
                actual[i] = args.get(i);
        return actual;
    }

    private static Object[] actualArguments(Executable e, List<Object> args) {
        if (e.isVarArgs())
            return actualArgumentsVarArgs(e, args);
        else
            return actualArgumentsNoVarArgs(e, args);
    }

    public static Object constructor(Object self, List<Object> args) {
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

    private static Object method(Object self, String name, Class<?> clas, List<Object> args) {
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

    public static Object method(Object self, String name, List<Object> args) {
        Object result = method(self, name, self.getClass(), args);
        if (result == NOT_FOUND && self instanceof Class)
            result = method(self, name, (Class<?>)self, args);
        return result;
    }

}

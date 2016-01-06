package obscure.wrappers;

import java.lang.IllegalAccessException;
import java.lang.IllegalArgumentException;
import java.lang.InstantiationException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import obscure.core.Applicable;
import obscure.core.List;
import obscure.core.ObscureException;
import obscure.core.Procedure;
import obscure.core.Symbol;
import obscure.core.Wrapper;

public class ObjectWrapper implements Wrapper {

    @Override
    public Class<?> wrapClass() {
        return Object.class;
    }

    @Override
    public Class<?> parentClass() {
        return null;
    }

    @Override
    public Applicable applicable(Symbol method, Object self) {
        Applicable a = map.get(method);
        if (a != null)
            return a;
        return method(method.name);
    }

    @Override
    public String print(Object self) {
        if (self == null)
            return "null";
        return self.toString();
    }
    
    public static final ObjectWrapper VALUE = new ObjectWrapper();

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

    static java.util.List<Object> list(List args) {
        java.util.List<Object> r = new ArrayList<>();
        for (Object e : args)
            r.add(e);
        return r;
    }
    
    static Object[] actualArgumentsVarArgs(Executable e, List args) {
        java.util.List<Object> list = list(args);
        Parameter[] parms = e.getParameters();
        int parmSize = parms.length;
        if (parmSize - 1 > list.size())
            return null;
        java.util.List<Object> actual = new ArrayList<>();
        for (int i = 0; i < parmSize; ++i) {
            Parameter parm = parms[i];
            Class<?> type = parm.getType();
            // TODO: VarArgsとして配列が指定された場合はそのまま渡す。
            if (parm.isVarArgs()) {
                int varSize = list.size() - i;
                Class<?> ctype = type.getComponentType();
                Object vars = Array.newInstance(ctype, varSize);
                for (int j = 0; j < varSize; ++j) {
                    Object arg = list.get(i + j);
                    if (ctype.isPrimitive()) {
                        if (arg == null || !PRIMITIVES.get(ctype).isInstance(arg))
                            return null;
                    } else if (arg != null && !ctype.isInstance(arg))
                        return null;
                    Array.set(vars, j, arg);
                }
                actual.add(vars);
            } else {
                Object arg = list.get(i);
                if (type.isPrimitive()) {
                    if (arg == null || !PRIMITIVES.get(type).isInstance(arg))
                        return null;
                } else if (arg != null && !type.isInstance(arg))
                    return null;
                actual.add(arg);
            }

        }
        return actual.toArray();
    }
    
    static Object[] actualArgumentsNoVarArgs(Executable e, List args) {
        java.util.List<Object> list = list(args);
        int parmSize = e.getParameterCount();
        if (parmSize != list.size())
            return null;
        Class<?>[] parmType = e.getParameterTypes();
        for (int i = 0; i < parmSize; ++i) {
            Class<?> type = parmType[i];
            Object arg = list.get(i);
            if (type.isPrimitive()) {
                if (arg == null || !PRIMITIVES.get(type).isInstance(arg))
                    return null;
            } else if (arg != null && !type.isInstance(arg))
                return null;
        }
        return list.toArray();
    }

    static Object[] actualArguments(Executable e, List args) {
        if (e.isVarArgs())
            return actualArgumentsVarArgs(e, args);
        else
            return actualArgumentsNoVarArgs(e, args);
    }

    static Applicable FIELD = (self, args, env) -> {
        Class<?> cls = self.getClass();
        String member = ((Symbol)args.car()).name;
        try {
            Field f = cls.getField(member);
            return f.get(self);
        } catch (NoSuchFieldException n) {
            if (self instanceof Class) {
                try {
                    Field f = ((Class<?>)self).getField(member);
                    return f.get(self);
                } catch (NoSuchFieldException
                        | SecurityException
                        | IllegalArgumentException
                        | IllegalAccessException p) {
                    throw new ObscureException(p);
                }
            }
            throw new ObscureException("no fields %s in %s", member, self);
        } catch (SecurityException
                | IllegalArgumentException
                | IllegalAccessException e) {
            throw new ObscureException(e);
        }
    };

    static Procedure CONSTRUCTOR = (self, args) -> {
        for (Constructor<?> c : ((Class<?>)self).getConstructors()) {
            Object[] actual = actualArguments(c, args);
            if (actual == null)
                continue;
            try {
                return c.newInstance(actual);
            } catch (InstantiationException
                    | IllegalAccessException
                    | IllegalArgumentException
                    | InvocationTargetException e) {
                throw new ObscureException(e);
            }
        }
        throw new ObscureException(
            "no constructor for new %s%s", self, args);
    };
    
    static Procedure method(String member) {
        return (self, args) -> {
            Class<?> cls = self.getClass();
            for (Method m : cls.getMethods()) {
                if (!m.getName().equals(member))
                    continue;
                Object[] actual = actualArguments(m, args);
                if (actual == null)
                    continue;
                try {
                    m.setAccessible(true);
                    return m.invoke(self, actual);
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    throw new ObscureException(e);
                }
            }
            if (self instanceof Class) {
                cls = (Class<?>)self;
                for (Method m : cls.getMethods()) {
                    if (!m.getName().equals(member))
                        continue;
                    Object[] actual = actualArguments(m, args);
                    if (actual == null)
                        continue;
                    try {
                        m.setAccessible(true);
                        return m.invoke(self, actual);
                    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                        throw new ObscureException(e);
                    }
                }
            }
            throw new ObscureException(
                "no methods %s in %s", member, cls);
        };
    }

    public static final Map<Symbol, Applicable> map = new HashMap<>();
    static {
        map.put(Symbol.of("new"), CONSTRUCTOR);
        map.put(Symbol.of("@"), FIELD);
    }

}

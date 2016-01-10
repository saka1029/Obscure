package obscure.wrappers;

import java.lang.reflect.Array;
import java.lang.reflect.Executable;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import obscure.core.List;
import obscure.core.Procedure;

public abstract class ExecutableProcedure implements Procedure {

    ExecutableProcedure() {}
    
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

    static Object[] asArray(List args) {
        int size = args.size();
        Object[] r = new Object[size];
        int i = 0;
        for (Object e : args)
            r[i++] = e;
        return r;
    }
    
    static boolean isAssignable(Class<?> parmType, Object arg) {
        if (parmType.isPrimitive()) {
            if (arg == null || !PRIMITIVES.get(parmType).isInstance(arg))
                return false;
        } else if (arg != null && !parmType.isInstance(arg))
            return false;
        return true;
    }

    static Object[] actualArgumentsVarArgs(Executable e, List args) {
        Parameter[] parms = e.getParameters();
        int parmSize = parms.length;
        if (parmSize - 1 > args.size())
            return null;
        Object[] list = asArray(args);
        Object[] actual = new Object[parmSize];
        for (int i = 0; i < parmSize; ++i) {
            Parameter parm = parms[i];
            Class<?> type = parm.getType();
            // TODO: VarArgsとして配列が指定された場合はそのまま渡す。
            if (parm.isVarArgs()) {
                int varSize = list.length - i;
                Class<?> ctype = type.getComponentType();
                Object vars = Array.newInstance(ctype, varSize);
                for (int j = 0; j < varSize; ++j) {
                    Object arg = list[i + j];
                    if (!isAssignable(ctype, arg))
                        return null;
                    Array.set(vars, j, arg);
                }
                actual[i] = vars;
            } else {
                Object arg = list[i];
                if (!isAssignable(type, arg))
                    return null;
                actual[i] = arg;
            }
        }
        return actual;
    }
    
    static Object[] actualArgumentsNoVarArgs(Executable e, List args) {
        int parmSize = e.getParameterCount();
        if (parmSize != args.size())
            return null;
        Object[] list = asArray(args);
        Class<?>[] parmType = e.getParameterTypes();
        for (int i = 0; i < parmSize; ++i)
            if (!isAssignable(parmType[i], list[i]))
                return null;
        return list;
    }

    static Object[] actualArguments(Executable e, List args) {
        if (e.isVarArgs())
            return actualArgumentsVarArgs(e, args);
        else
            return actualArgumentsNoVarArgs(e, args);
    }

}

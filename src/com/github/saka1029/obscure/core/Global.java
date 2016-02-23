package com.github.saka1029.obscure.core;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Global {

    public static boolean DEBUG = false;
    
    public static Object read(String s) {
        return new Reader(s).read();
    }

    static int INDENT = 0;
    static final String SPACES = String.format("%4000s", "");

    static String indent(int n) {
        return SPACES.substring(0, n * 2);
    }

    public static Object eval(Object obj, Env env) {
        if (DEBUG)
            System.out.printf("%s>eval %s%n", indent(INDENT++), print(obj));
        Object result = obj;
        if (obj instanceof Evalable)
            result = ((Evalable)obj).eval(env);
        if (DEBUG)
            System.out.printf("%s<eval %s%n", indent(--INDENT), print(result));
        return result;
    }
    
    public static Object invoke(Object obj, Object self, Env env) {
        if (DEBUG)
            System.out.printf("%s>invoke %s %s%n", indent(INDENT++), print(self), print(obj));
        if (self == null) {
            INDENT = 0;
            throw new ObscureException("cannot invoke %s for %s", print(obj), print(self));
        }
        if (obj instanceof Invokable) {
            Object r = ((Invokable)obj).invoke(self, env);
            if (DEBUG)
                System.out.printf("%s<invoke %s%n", indent(--INDENT), print(r));
            return r;
        }
        INDENT = 0;
        throw new ObscureException("cannot invoke %s for %s", obj, self);
    }
    
    public static String print(Object obj) {
        if (obj == null)
            return "null";
        Object value = getClassEnv(obj.getClass(), sym("print"));
        if (value instanceof Applicable)
            return (String)((Applicable)value).apply(obj, Nil.VALUE, Env.EMPTY);
        return obj.toString();
    }
    
    public static final Env ENV = new MapEnv(null);

    public static void defineGlobalEnv(String key, Object value) {
        ENV.define(sym(key), value);
    }
    
    public static final Class<?>[] APPLICABLE_ARGS_TYPES = { Object.class, List.class, Env.class };
    public static final Class<?>[] PROCEDURE_ARGS_TYPES = { Object.class, List.class };
    public static final Class<?>[] MACRO_ARGS_TYPES = { List.class };

    public static void defineGlobalEnv(Class<?> cls) {
        for (Field f : cls.getFields()) {
            if ((f.getModifiers() & Modifier.STATIC) == 0) continue;
            String name = f.getName();
            ObscureName n = f.getAnnotation(ObscureName.class);
            if (n != null && n.value() != null)
                name = n.value();
            try {
                defineGlobalEnv(name, f.get(null));
            } catch (IllegalArgumentException | IllegalAccessException e) {
                new ObscureException(e);
            }
        }
        for (Method m : cls.getMethods()) {
            if ((m.getModifiers() & Modifier.STATIC) == 0) continue;
            String name = m.getName();
            ObscureName n = m.getAnnotation(ObscureName.class);
            if (n != null && n.value() != null)
                name = n.value();
            Class<?>[] parms = m.getParameterTypes();
            Applicable value = null;
            if (Arrays.equals(parms, APPLICABLE_ARGS_TYPES))
                value = (Applicable)(self, args, env) -> {
                    try {
                        return m.invoke(null, self, args, env);
                    } catch (IllegalAccessException
                            | IllegalArgumentException
                            | InvocationTargetException e) {
                        throw new ObscureException(e);
                    }
                };
            else if (Arrays.equals(parms, PROCEDURE_ARGS_TYPES))
                value = (Procedure)(self, args) -> {
                    try {
                        return m.invoke(null, self, args);
                    } catch (IllegalAccessException
                            | IllegalArgumentException
                            | InvocationTargetException e) {
                        throw new ObscureException(e);
                    }
                };
            else if (Arrays.equals(parms, MACRO_ARGS_TYPES))
                value = (Macro)(args) -> {
                    try {
                        return m.invoke(null, args);
                    } catch (IllegalAccessException
                            | IllegalArgumentException
                            | InvocationTargetException e) {
                        throw new ObscureException(e);
                    }
                };
            if (value != null)
                defineGlobalEnv(name, value);
        }
    }

    public static final Map<Symbol, Map<Class<?>, Object>> CLASS_ENV = new HashMap<>();
    
    public static void defineClassEnv(Class<?> cls, String key, Object value) {
        CLASS_ENV.computeIfAbsent(sym(key), k -> new HashMap<>()).put(cls, value);
    }
    
    public static Object getClassEnv(Class<?> cls, Symbol symbol) {
//        if (cls.isArray())
//            cls = Object[].class;
        Map<Class<?>, Object> methodEnv = CLASS_ENV.get(symbol);
        if (methodEnv == null) return null;
        Object value = methodEnv.get(cls);
        if (value != null) return value;
        // search interfaces
        for (Class<?> c : cls.getInterfaces()) {
            value =  methodEnv.get(c);
            if (value != null) return value;
        }
        // search super classes
        for (Class<?> c = cls.getSuperclass(); cls != null; cls = cls.getSuperclass()) {
            value = methodEnv.get(c);
            if (value != null) return value;
        }
        return Reflection.NOT_FOUND;
    }
    
    public static void defineClassEnv(Class<?> extender) {
        for (Method m : extender.getMethods()) {
            if ((m.getModifiers() & Modifier.STATIC) == 0) continue;
            if (m.getParameters().length < 1) continue;
            Parameter[] p = m.getParameters();
            if (p.length < 1) continue;
            Class<?> clas = p[0].getType();
//            MethodClass c = m.getAnnotation(MethodClass.class);
//            if (c != null && c.value() != null)
//                clas = c.value();
            if (clas.isPrimitive())
                clas = Reflection.getPrimitive(clas);
            String name = m.getName();
            ObscureName n = m.getAnnotation(ObscureName.class);
            if (n != null && n.value() != null)
                name = n.value();
            Map<Class<?>, Object> map = CLASS_ENV.computeIfAbsent(sym(name), k -> new HashMap<>());
            Object value = map.computeIfAbsent(clas, k -> new StaticMethodProcedure());
            if (!(value instanceof StaticMethodProcedure))
                throw new ObscureException("class %s name %s is not procedure", clas, name);
//            System.out.println(clas + ":" + m);
            ((StaticMethodProcedure)value).add(m);
        }
    }

    static Class<?> commonSuperClass(List list) {
        Class<?> common = null;
        for (Object e : list)
            if (e != null)
                if (common == null)
                    common = e.getClass();
                else
                    while (!common.isAssignableFrom(e.getClass()))
                        common = common.getSuperclass();
        if (common == null)
            common = Object.class;
        return common;
    }

    static {
        defineGlobalEnv(StandardGlobal.class);
        defineClassEnv(StandardMethods.class);
        defineClassEnv(Integer.class);
//        defineClassEnv(Boolean.class, "if", (Applicable)(self, args, env) -> (boolean)self ? eval(car(args), env) : eval(cadr(args), env));
    }
    
    public static Object car(Object obj) {
        if (!(obj instanceof List))
            throw new ObscureException("cannot car of %s", obj);
        return ((List)obj).car();
    }
    
    public static Object cdr(Object obj) {
        if (!(obj instanceof List))
            throw new ObscureException("cannot cdr of %s", obj);
        return ((List)obj).cdr();
    }
    
    public static Object cadr(Object obj) {
        return car(cdr(obj));
    }
    
    public static Object caddr(Object obj) {
        return car(cdr(cdr(obj)));
    }
    
    public static Object cons(Object car, Object cdr) {
        return Pair.of(car, cdr);
    }
    
    public static List asList(Object obj) {
        return (List)obj;
    }

    public static List list(Object... args) {
        return Pair.list(args);
    }
    
    public static Symbol sym(String name) {
        return Symbol.of(name);
    }
}

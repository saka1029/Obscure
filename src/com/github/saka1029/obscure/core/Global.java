package com.github.saka1029.obscure.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
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

    public static final Map<Symbol, Map<Class<?>, Object>> CLASS_ENV = new HashMap<>();
    
    public static void defineClassEnv(Class<?> cls, String key, Object value) {
        CLASS_ENV.computeIfAbsent(sym(key), k -> new HashMap<>()).put(cls, value);
    }
    
    public static Object getClassEnv(Class<?> cls, Symbol symbol) {
        if (cls.isArray())
            cls = Object[].class;
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
        return null;
    }
    
    public static void defineClassEnv(Class<?> extender) {
        for (Method m : extender.getMethods()) {
            if ((m.getModifiers() & Modifier.STATIC) == 0) continue;
            if (m.getParameters().length < 1) continue;
            Parameter[] p = m.getParameters();
            if (p.length < 1) continue;
            Class<?> clas = p[0].getType();
            if (clas.isPrimitive())
                clas = Reflection.getPrimitive(clas);
            String name = m.getName();
            Annotation a = m.getAnnotation(MethodName.class);
            if (a != null) {
                String value = ((MethodName)a).value();
                if (value != null && value.length() > 0)
                    name = value;
            }
            Map<Class<?>, Object> map = CLASS_ENV.computeIfAbsent(sym(name), k -> new HashMap<>());
            Object value = map.computeIfAbsent(clas, k -> new MethodProcedure());
            if (!(value instanceof MethodProcedure))
                throw new ObscureException("class %s name %s is not procedure", clas, name);
//            System.out.println(clas + ":" + m);
            ((MethodProcedure)value).add(m);
        }
    }

    static {
        defineGlobalEnv("exit", Reader.EOF_OBJECT);
        defineGlobalEnv("Class", Class.class);
        defineGlobalEnv("Integer", Integer.class);
        defineGlobalEnv("int", Integer.TYPE);
        defineGlobalEnv("car", (Procedure)(self, args) -> car(car(args)));
        defineGlobalEnv("cdr", (Procedure)(self, args) -> cdr(car(args)));
        defineGlobalEnv("cons", (Procedure)(self, args) -> cons(car(args), cadr(args)));
        defineGlobalEnv("null?", (Procedure)(self, args) -> car(args) == Nil.VALUE);
        defineGlobalEnv("if", (Applicable)(self, args, env) -> (boolean)eval(car(args), env) ? eval(cadr(args), env) : eval(caddr(args), env));
        defineGlobalEnv("define", (Applicable)(self, args, env) -> {
            Object parms = car(args);
            if (parms instanceof Symbol)
                return env.define((Symbol)parms, eval(cadr(args), env));  
            else if (parms instanceof List)
                return env.define((Symbol)car(parms), Closure.of((List)cdr(parms), (List)cdr(args), env));
            else
                throw new ObscureException("cannot define %s", args);
        });
        defineGlobalEnv("import", (Applicable)(self, args, env) -> {
            try {
                if (((List)args).size() == 2) {
                    String name = (String)eval(cadr(args), env);
                    return env.define((Symbol)car(args), Class.forName(name));
                } else {
                    String name = (String)eval(car(args), env);
                    return env.define(sym(name.replaceFirst("^.*\\.", "")), Class.forName(name));
                }
            } catch (ClassNotFoundException e) {
                throw new ObscureException(e);
            }
        });
        defineGlobalEnv("lambda", (Applicable)(self, args, env) -> Closure.of((List)car(args), (List)cdr(args), env));
        defineGlobalEnv("let", (Macro)(args) -> {
            Pair.Builder parms = Pair.builder();
            Pair.Builder actual = Pair.builder();
            for (Object e : (List)car(args)) {
                parms.tail(car(e));
                actual.tail(cadr(e));
            }
            return cons(cons(sym("lambda"), cons(parms.build(), cdr(args))), actual.build());
        });
        defineGlobalEnv("quote", (Applicable)(self, args, env) -> car(args));
        defineGlobalEnv("+", new GenericOperator(sym("+")));
        defineGlobalEnv("-", new GenericOperator(sym("-")));
        defineGlobalEnv("*", new GenericOperator(sym("*")));
        defineGlobalEnv("<", new CompareOperator(sym("<0")));
        defineGlobalEnv("<=", new CompareOperator(sym("<=0")));
        defineGlobalEnv(">", new CompareOperator(sym(">0")));
        defineGlobalEnv(">=", new CompareOperator(sym(">=0")));
        defineGlobalEnv("<>", new CompareOperator(sym("<>0")));
        defineGlobalEnv("==", new CompareOperator(sym("==0")));
        defineGlobalEnv("print", (Macro)(args) -> list(car(args), list(sym("print"))));
        defineGlobalEnv("array", (Procedure)(self, args) -> Reflection.method(Array.class, "newInstance", args.toArray()));
    
        defineClassEnv(StandardExtender.class);
        defineClassEnv(Integer.class);
        defineClassEnv(Object[].class, "get", (Procedure)(self, args) -> Array.get(self, (int)car(args)));
        defineClassEnv(Object[].class, "set", (Procedure)(self, args) -> {
            Object value = cadr(args);
            Array.set(self, (int)car(args), value);
            return value;
        });
        defineClassEnv(Object[].class, "length", (Procedure)(self, args) -> Array.getLength(self));
        defineClassEnv(Boolean.class, "if", (Applicable)(self, args, env) -> (boolean)self ? eval(car(args), env) : eval(cadr(args), env));
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

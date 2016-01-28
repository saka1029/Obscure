package com.github.saka1029.obscure.core;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;

public class Global {

    public static boolean DEBUG = false;
    
    public static Object read(String s) throws IOException {
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
        Object value = getClassEnv(obj.getClass(), Symbol.of("print"));
        if (value instanceof Applicable)
            return (String)((Applicable)value).apply(obj, Nil.VALUE, Env.EMPTY);
        return obj.toString();
    }
    
    public static final Env ENV = new MapEnv(null);

    public static void defineGlobalEnv(String key, Object value) {
        ENV.define(Symbol.of(key), value);
    }

    public static final Map<Symbol, Map<Class<?>, Object>> CLASS_ENV = new HashMap<>();
    
    public static void defineClassEnv(Class<?> cls, String key, Object value) {
        CLASS_ENV.computeIfAbsent(Symbol.of(key), k -> new HashMap<>()).put(cls, value);
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
    
    static {
        defineGlobalEnv("exit", Reader.EOF_OBJECT);
        defineGlobalEnv("Class", Class.class);
        defineGlobalEnv("Integer", Integer.class);
        defineGlobalEnv("int", Integer.TYPE);
        defineGlobalEnv("car", (Procedure)(self, args) -> car(car(args)));
        defineGlobalEnv("cdr", (Procedure)(self, args) -> cdr(car(args)));
        defineGlobalEnv("cons", (Procedure)(self, args) -> Pair.of(car(args), cadr(args)));
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
                    return env.define(Symbol.of(name.replaceFirst("^.*\\.", "")), Class.forName(name));
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
            return Pair.of(Pair.of(Symbol.of("lambda"), Pair.of(parms.build(), cdr(args))), actual.build());
        });
        defineGlobalEnv("quote", (Applicable)(self, args, env) -> car(args));
        defineGlobalEnv("+", new GenericOperator(Symbol.of("+")));
        defineGlobalEnv("-", new GenericOperator(Symbol.of("-")));
        defineGlobalEnv("*", new GenericOperator(Symbol.of("*")));
        defineGlobalEnv("<", new GenericOperator(Symbol.of("<")));
        defineGlobalEnv("print", (Macro)(args) -> list(car(args), list(Symbol.of("print"))));
        defineGlobalEnv("array", (Procedure)(self, args) -> Reflection.method(Array.class, "newInstance", args.toArray()));
    
        defineClassEnv(Object[].class, "get", (Procedure)(self, args) -> Array.get(self, (int)car(args)));
        defineClassEnv(Object[].class, "set", (Procedure)(self, args) -> {
            Object value = cadr(args);
            Array.set(self, (int)car(args), value);
            return value;
        });
        defineClassEnv(Object[].class, "length", (Procedure)(self, args) -> Array.getLength(self));
        defineClassEnv(Boolean.class, "if", (Applicable)(self, args, env) -> (boolean)self ? eval(car(args), env) : eval(cadr(args), env));
        defineClassEnv(Integer.class, "+", (Procedure)(self, args) -> ((int)self) + ((int)car(args)));
        defineClassEnv(Integer.class, "-", (Procedure)(self, args) -> ((int)self) - ((int)car(args)));
        defineClassEnv(Integer.class, "*", (Procedure)(self, args) -> ((int)self) * ((int)car(args)));
        defineClassEnv(Integer.class, "<", (Procedure)(self, args) -> ((int)self) < ((int)car(args)));
        defineClassEnv(String.class, "+", (Procedure)(self, args) -> ((String)self) + car(args));
        defineClassEnv(String.class, "<", (Procedure)(self, args) -> ((String)self).compareTo((String)car(args)) < 0);
        defineClassEnv(String.class, "print", (Procedure)(self, args) -> {
            String s = (String)self;
            StringBuilder sb = new StringBuilder();
            sb.append("\"");
            for (int i = 0, size = s.length(); i < size; ++i) {
                char c = s.charAt(i);
                switch (c) {
                    case '\b': sb.append("\\b"); break;
                    case '\f': sb.append("\\f"); break;
                    case '\t': sb.append("\\t"); break;
                    case '\n': sb.append("\\n"); break;
                    case '\r': sb.append("\\r"); break;
                    case '"': sb.append("\\\""); break;
                    default: sb.append(c); break;
                }
            }
            sb.append("\"");
            return sb.toString();
        });
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
    
    public static List asList(Object obj) {
        return (List)obj;
    }

    public static List list(Object... args) {
        return Pair.list(args);
    }
}

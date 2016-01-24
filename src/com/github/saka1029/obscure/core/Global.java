package com.github.saka1029.obscure.core;

import java.io.IOException;
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
            System.out.printf("%s>eval %s%n", indent(INDENT++), obj);
        Object result = obj;
        if (obj instanceof Evalable)
            result = ((Evalable)obj).eval(env);
        if (DEBUG)
        System.out.printf("%s<eval %s%n", indent(--INDENT), result);
        return result;
    }
    
    public static Object invoke(Object obj, Object self, Env env) {
        if (DEBUG)
        System.out.printf("%s>invoke %s %s%n", indent(INDENT++), self, obj);
        if (self == null) {
            INDENT = 0;
            throw new ObscureException("cannot invoke %s for %s", obj, self);
        }
        if (obj instanceof Invokable) {
            Object r = ((Invokable)obj).invoke(self, env);
            if (DEBUG)
                System.out.printf("%s<invoke %s%n", indent(--INDENT), r);
            return r;
        }
        INDENT = 0;
        throw new ObscureException("cannot invoke %s for %s", obj, self);
    }
    
    public static String print(Object obj) {
        if (obj == null)
            return "null";
        return obj.toString();
    }
    
    public static final Env ENV = new MapEnv(null);

    public static void defineGlobal(String key, Object value) {
        ENV.define(Symbol.of(key), value);
    }

    public static final Map<Class<?>, Env> CLASS_ENV = new HashMap<>();
    
    public static void defineClass(Class<?> cls, String key, Object value) {
        CLASS_ENV.computeIfAbsent(cls, k -> new MapEnv(null))
            .define(Symbol.of(key), value);
    }
    
    static {
        defineGlobal("exit", Reader.EOF_OBJECT);
        defineGlobal("Class", Class.class);
        defineGlobal("car", (Procedure)(self, args) -> car(car(args)));
        defineGlobal("cdr", (Procedure)(self, args) -> cdr(car(args)));
        defineGlobal("cons", (Procedure)(self, args) -> Pair.of(car(args), cadr(args)));
        defineGlobal("null?", (Procedure)(self, args) -> car(args) == Nil.VALUE);
        defineGlobal("if", (Applicable)(self, args, env) -> (boolean)eval(car(args), env) ? eval(cadr(args), env) : eval(caddr(args), env));
        defineGlobal("define", (Applicable)(self, args, env) -> {
            Object parms = car(args);
            if (parms instanceof Symbol)
                return env.define((Symbol)parms, eval(cadr(args), env));  
            else if (parms instanceof List)
                return env.define((Symbol)car(parms), Closure.of((List)cdr(parms), (List)cdr(args), env));
            else
                throw new ObscureException("cannot define %s", args);
        });
        defineGlobal("lambda", (Applicable)(self, args, env) -> Closure.of((List)car(args), (List)cdr(args), env));
        defineGlobal("let", (Macro)(args) -> {
            Pair.Builder parms = Pair.builder();
            Pair.Builder actual = Pair.builder();
            for (Object e : (List)car(args)) {
                parms.tail(car(e));
                actual.tail(cadr(e));
            }
            return Pair.of(Pair.of(Symbol.of("lambda"), Pair.of(parms.build(), cdr(args))), actual.build());
        });
        defineGlobal("quote", (Applicable)(self, args, env) -> car(args));
        defineGlobal("+", new GenericOperator(Symbol.of("+")));
        defineGlobal("-", new GenericOperator(Symbol.of("-")));
        defineGlobal("*", new GenericOperator(Symbol.of("*")));
        defineGlobal("<", new GenericOperator(Symbol.of("<")));
    
        defineClass(Boolean.class, "if", (Applicable)(self, args, env) -> (boolean)self ? eval(car(args), env) : eval(cadr(args), env));
        defineClass(Integer.class, "+", (Procedure)(self, args) -> ((int)self) + ((int)car(args)));
        defineClass(Integer.class, "-", (Procedure)(self, args) -> ((int)self) - ((int)car(args)));
        defineClass(Integer.class, "*", (Procedure)(self, args) -> ((int)self) * ((int)car(args)));
        defineClass(Integer.class, "<", (Procedure)(self, args) -> ((int)self) < ((int)car(args)));
        defineClass(String.class, "+", (Procedure)(self, args) -> ((String)self) + car(args));
        defineClass(String.class, "<", (Procedure)(self, args) -> ((String)self).compareTo((String)car(args)) < 0);
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

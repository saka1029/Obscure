package io.github.saka1029.obscure.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Obscure {

    private Obscure() {}
    
    static Object invoke(Object target, Object invoke, Env env) {
        Class<?> clas = target.getClass();
        if (CLASS_ENV.containsKey(clas)) {
            Env cenv = CLASS_ENV.get(clas);
            if (cenv != null)
                if (isSymbol(invoke))
                    return cenv.get((Symbol)invoke);
                else if (isList(invoke)) {
                    Object app = cenv.get((Symbol)car(invoke));
                    if (app instanceof Applicable)
                        return ((Applicable)app).apply(target, cdr(invoke), env);
                }
        }
        if (isSymbol(invoke)) {
            String name = asSymbol(invoke).name;
             Object result = Reflection.field(target, name);
             if (result == Reflection.NOT_FOUND)
                 throw new ObscureException("no such field %s", name);
             return result;
        } else if (isList(invoke)) {
            List<Object> invokeList = asList(invoke);
            Object first = invokeList.get(0);
            if (isSymbol(first)) {
                String name = asSymbol(first).name;
                Procedure method;
                if (name.equals("new"))
                    method = (self, args) -> Reflection.constructor(self, args);
                else
                    method = (self, args) -> Reflection.method(self, name, args);
                Object result = method.apply(target, cdr(invokeList));
                if (result == Reflection.NOT_FOUND)
                    throw new ObscureException("method %s not found", first);
                return result;
            } else
                throw  new ObscureException("method must be symbol but %s", first);
              
        } else
            throw new ObscureException("cannot invoke %s", invoke);
    }

    public static Object eval(Object object, Env env) {
        System.out.println("eval: " + object);
        if (isSymbol(object))
            return env.get(asSymbol(object));
        if (isList(object)) {
            List<Object> list = asList(object);
            int size = list.size();
            if (size <= 0)
                return object;
            Object result = eval(car(list), env);
            if (result instanceof Applicable)
                return ((Applicable)result).apply(null, cdr(list), env);
            for (int i = 1; i < size; ++i)
                result = invoke(result, list.get(i), env);
            return result;
        }
        return object;
    }
 
    public static String print(Object object) {
        return String.valueOf(object);
    }
 
    static final Env GLOBAL_ENV = new MapEnv(null);
 
    static {
        GLOBAL_ENV.define(sym("quit"), Reader.EOF_OBJECT);
        GLOBAL_ENV.define(sym("quote"), (Applicable)(self, args, env) -> car(args));
        GLOBAL_ENV.define(sym("car"), (Procedure)(self, args) -> car(car(args)));
        GLOBAL_ENV.define(sym("cdr"), (Procedure)(self, args) -> cdr(car(args)));
        GLOBAL_ENV.define(sym("lambda"), (Applicable)(self, args, env) -> Closure.of(asList(car(args)), cdr(args), env));
        GLOBAL_ENV.define(sym("define"), new Define());
        GLOBAL_ENV.define(sym("let"), new Let());
        GLOBAL_ENV.define(sym("Class"), Class.class);
        GLOBAL_ENV.define(sym("+"), new GenericOperator(sym("+")));
    }
 
    static final Map<Class<?>, Env> CLASS_ENV = new HashMap<>();

    static {
        CLASS_ENV.put(String.class, new MapEnv(null) {{
            define(sym("+"), (Procedure) (self, args) -> "" + self + car(args));
        }});
        CLASS_ENV.put(Integer.class, new MapEnv(null) {{
            define(sym("+"), (Procedure) (self, args) -> ((int)self) + ((int)car(args)));
        }});
    }
 
    // Helper functions

    public static Env env() {
        return new MapEnv(GLOBAL_ENV);
    }
    
    public static Env env(Env env) {
        return new MapEnv(env);
    }
    
    public static boolean isList(Object object) {
        return object instanceof List<?>;
    }
    
    @SuppressWarnings("unchecked")
    public static List<Object> asList(Object object) {
        return (List<Object>)object;
    }

    public static List<Object> list(Object... args) {
        List<Object> r = new ArrayList<>();
        for (Object e : args)
            r.add(e);
        return r;
    }
    
    public static List<Object> append(List<Object> a, List<Object> b) {
        a.addAll(b);
        return a;
    }
    
    public static List<Object> cdr(Object list) {
        return ImmutableList.cdr(asList(list));
    }
 
    public static List<Object> cdr(Object list, int n) {
        return ImmutableList.cdr(asList(list), n);
    }
 
    public static Object car(Object list) {
        return asList(list).get(0);
    }
    
    public static Object cadr(Object list) {
        return car(cdr(list));
    }
    
    public static Object caddr(Object list) {
        return car(cdr(list, 2));
    }
    
    public static Object cadddr(Object list) {
        return car(cdr(list, 3));
    }
    
    public static Object read(String s) throws IOException {
        return new Reader(s).read();
    }

    public static Symbol sym(String name) {
        return Symbol.of(name);
    }
    
    public static boolean isSymbol(Object object) {
        return object instanceof Symbol;
    }
    
    public static Symbol asSymbol(Object object) {
        return (Symbol)object;
    }
    
}

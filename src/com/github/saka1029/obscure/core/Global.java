package com.github.saka1029.obscure.core;

import java.util.HashMap;
import java.util.Map;

public class Global {

    public static Object eval(Object obj, Env env) {
        if (obj instanceof Evalable)
            return ((Evalable)obj).eval(env);
        return obj;
    }
    
    public static Object invoke(Object obj, Object self, Env env) {
        if (obj instanceof Invokable)
            return ((Invokable)obj).invoke(self, env);
        return obj;
    }
    
    public static String print(Object obj) {
        if (obj == null)
            return "null";
        return obj.toString();
    }
    
    static final Env ENV = new MapEnv(null) {{
        define(Symbol.of("exit"), Reader.EOF_OBJECT);
        define(Symbol.of("Class"), Class.class);
        define(Symbol.of("define"), (Applicable)(self, args, env) -> env.define((Symbol)car(args), eval(cadr(args), env)));
        define(Symbol.of("+"), new GenericOperator(Symbol.of("+")));
    }};
    
    static final Map<Class<?>, Env> CLASS_ENV = new HashMap<Class<?>, Env>() {{
        put(Integer.class, new MapEnv(null) {{
            define(Symbol.of("+"), (Procedure)(self, args) -> ((int)self) + ((int)car(args)));
        }});
        put(String.class, new MapEnv(null) {{
            define(Symbol.of("+"), (Procedure)(self, args) -> ((String)self) + car(args));
        }});
    }};
    
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
}

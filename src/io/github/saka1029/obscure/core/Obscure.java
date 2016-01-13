package io.github.saka1029.obscure.core;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Obscure {

    private Obscure() {}
    
    public static Object eval(Object object, Environment env) {
        if (isSymbol(object))
            return env.get(asSymbol(object));
        if (isList(object)) {
            List<Object> list = asList(object);
            if (list.size() <= 0)
                return object;
            Object first = eval(car(list), env);
            if (first instanceof Applicable)
                return ((Applicable)first).apply(null, cdr(list), env);
            else
                throw new ObscureException("cannot eval %s", object);
        }
        return object;
    }
 
    public static String print(Object object) {
        return String.valueOf(object);
    }
 
    static final Environment GLOBAL = Environment.create(null);
    
    static {
        GLOBAL.define(sym("quote"), (Applicable)(self, args, env) -> car(args));
        GLOBAL.define(sym("car"), (Procedure)(self, args) -> car(car(args)));
        GLOBAL.define(sym("cdr"), (Procedure)(self, args) -> cdr(car(args)));
        GLOBAL.define(sym("lambda"), (Applicable)(self, args, env) -> Closure.of(asList(car(args)), cdr(args), env));
        GLOBAL.define(sym("define"), new Define());
    }

    public static Environment env() {
        return Environment.create();
    }
    
    public static Environment env(Environment env) {
        return Environment.create(env);
    }
    
    public static boolean isList(Object object) {
        return object instanceof List<?>;
    }
    
    @SuppressWarnings("unchecked")
    public static List<Object> asList(Object object) {
        return (List<Object>)object;
    }

    public static List<Object> list(Object... args) {
        return Arrays.asList(args);
    }
    
    public static List<Object> cdr(Object list) {
        return ImmutableList.cdr(asList(list));
    }
 
    public static Object car(Object list) {
        return asList(list).get(0);
    }
    
    public static Object cadr(Object list) {
        return car(cdr(list));
    }
    
    public static Object caddr(Object list) {
        return car(cdr(cdr(list)));
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

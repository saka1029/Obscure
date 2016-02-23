package com.github.saka1029.obscure.core;

import java.lang.reflect.Array;
import static com.github.saka1029.obscure.core.Global.*;

public class StandardGlobal {
    
    private StandardGlobal() {}
    
    @ObscureName("exit") public static final Object EXIT = Reader.EOF_OBJECT;
    @ObscureName("System")public static final Object SYSTEM = System.class;
    @ObscureName("Class")public static final Object CLASS = Class.class;
    @ObscureName("Integer") public static final Object INTEGER = Integer.class;
    @ObscureName("String") public static final Object STRING = String.class;
    @ObscureName("Array") public static final Object ARRAY = Array.class;
    @ObscureName("int") public static final Object INT = Integer.TYPE;
    @ObscureName("car") public static Object car0(Object self, List args) { return car(car(args)); }
    @ObscureName("cdr") public static Object cdr0(Object self, List args) { return cdr(car(args)); }
    @ObscureName("cons") public static Object cons0(Object self, List args) { return cons(car(args), cadr(args)); }
    @ObscureName("null?") public static Object null0(Object self, List args) { return car(args) == Nil.VALUE; }
    @ObscureName("if") public static Object if0(Object self, List args, Env env) {
        return (boolean)eval(car(args), env) ? eval(cadr(args), env) : eval(caddr(args), env);
    }
    @ObscureName("define") public static Object define0(Object self, List args, Env env) {
        Object parms = car(args);
        if (parms instanceof Symbol)
            return env.define((Symbol)parms, eval(cadr(args), env));  
        else if (parms instanceof List)
            // (define (list . x) x)
            return env.define((Symbol)car(parms), Closure.of(cdr(parms), (List)cdr(args), env));
        else
            throw new ObscureException("cannot define %s", args);
    }
    @ObscureName("set") public static Object set(Object self, List args, Env env) {
        Object parms = car(args);
        if (parms instanceof Symbol)
            return env.set((Symbol)parms, eval(cadr(args), env));  
        else if (parms instanceof List)
            // (define (list . x) x)
            return env.set((Symbol)car(parms), Closure.of(cdr(parms), (List)cdr(args), env));
        else
            throw new ObscureException("cannot set %s", args);
    }
    @ObscureName("import") public static Object import0(Object self, List args, Env env) {
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
    }
    @ObscureName("lambda") public static Object lambda(Object self, List args, Env env) {
        return Closure.of(car(args), (List)cdr(args), env);
    }
    @ObscureName("let") public static Object let(List args) {
        Pair.Builder parms = Pair.builder();
        Pair.Builder actual = Pair.builder();
        for (Object e : (List)car(args)) {
            parms.tail(car(e));
            actual.tail(cadr(e));
        }
        return cons(cons(sym("lambda"), cons(parms.build(), cdr(args))), actual.build());
    }
    @ObscureName("quote") public static Object quote(Object self, List args, Env env) { return car(args); }

    private static Object genericOperator(String operator, List args) {
        Pair.Builder r = Pair.builder();
        r.tail(car(args));
        for (Object e = cdr(args); e instanceof Pair; e = cdr(e))
            r.tail(list(sym(operator), car(e)));
        return r.build();
    }

    @ObscureName("+") public static Object plus(List args) { return genericOperator("+", args); }
    @ObscureName("-") public static Object minus(List args) { return genericOperator("-", args); }
    @ObscureName("*") public static Object multiply(List args) { return genericOperator("*", args); }
    @ObscureName("/") public static Object divide(List args) { return genericOperator("/", args); }
    
    private static Object compareOperator(String operator, List args) {
        return list(car(args), list(sym("compareTo"), cadr(args)), list(sym(operator)));
    }

    @ObscureName("<") public static Object lt(List args) { return compareOperator("<0", args); }
    @ObscureName("<=") public static Object le(List args) { return compareOperator("<=0", args); }
    @ObscureName(">") public static Object gt(List args) { return compareOperator(">0", args); }
    @ObscureName(">=") public static Object ge(List args) { return compareOperator(">=0", args); }
    @ObscureName("!=") public static Object ne(List args) { return compareOperator("!=0", args); }
    @ObscureName("==") public static Object eq(List args) { return compareOperator("==0", args); }
    @ObscureName("print") public static Object print(List args) { return list(car(args), list(sym("print"))); }
    @ObscureName("makeArray") public static Object makeArray(Object self, List args) {
        return Reflection.method(Array.class, "newInstance", args.toArray());
    }

    @ObscureName("array") public static Object array(Object self, List args) {
        Object array;
        if (car(args) instanceof Class<?>) {    // (array class values...)
            array = Array.newInstance((Class<?>)car(args), args.size() - 1);
            args = asList(cdr(args));
        } else                                  // (array values...)
            array = Array.newInstance(commonSuperClass(args), args.size());
        int i = 0;
        for (Object e : args)
            Array.set(array, i++, e);
        return array;
    }
    
    @ObscureName("vargs") public static Object vargs(Object self, List args) {
        args = asList(car(args));
        Object array = Array.newInstance(commonSuperClass(asList(args)), asList(args).size());
        int i = 0;
        for (Object e : args)
            Array.set(array, i++, e);
        return array;
    }

}

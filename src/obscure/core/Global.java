package obscure.core;

import obscure.wrappers.IntegerWrapper;
import obscure.wrappers.ObjectWrapper;
import obscure.wrappers.StringWrapper;

public class Global {

    static List asList(Object object) {
        return (List)object;
    }

    public static final Env ENV = Env.create(null);
    static {
//        ENV.define(Symbol.of("*"), ENV);
        ENV.define(Symbol.QUOTE, (Applicable)((self, args, env) -> asList(args).car()));
        ENV.define(Symbol.of("cons"), (Procedure)((self, args) -> Pair.of(asList(args).car(), asList(asList(args).cdr()).car())));
        ENV.define(Symbol.of("define"), (Applicable)((self, args, env) -> {
            Object arg1 = asList(args).car();
            Object arg2 = asList(asList(args).cdr()).car();
            Object value = Global.eval(arg2, env);
            return env.define((Symbol)arg1, value);
        }));
        ENV.define(Symbol.of("Class"), Class.class);
    }
    
    public static Object eval(Object object, Env env) {
        if (object instanceof Evalable)
            return ((Evalable)object).eval(env);
        return object;
    }
    
    public static Env wrap(Object object) {
        if (object instanceof Integer)
            return IntegerWrapper.VALUE;
        if (object instanceof String)
            return StringWrapper.VALUE;
        return ObjectWrapper.VALUE;
    }
    
}

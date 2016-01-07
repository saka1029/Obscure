package obscure.core;

import java.util.HashMap;
import java.util.Map;

import obscure.globals.*;
import obscure.wrappers.*;

public class Global {

    public static final Env ENV = Env.create(null);
    static {
        ENV.define(Symbol.of(";"), new Cascade());
        ENV.define(Symbol.of("Class"), Class.class);
        ENV.define(Symbol.of("cons"), new Cons());
        ENV.define(Symbol.of("define"), new Define());
        ENV.define(Symbol.of("defmacro"), new DefMacro());
        ENV.define(Symbol.of("expand"), new Expand());
        ENV.define(Symbol.of("lambda"), new Lambda());
        ENV.define(Symbol.of("let"), new Let());
        ENV.define(Symbol.of("macro"), new MakeMacro());
        ENV.define(Symbol.QUOTE, new Quote());
    }

    static final Map<Class<?>, Wrapper> map = new HashMap<>();

    static void add(Wrapper w) {
        map.put(w.wrapClass(), w);
    }
    
    static final Wrapper objectWrapper = new ObjectWrapper();

    static {
        add(new PairWrapper());
        add(new IntegerWrapper());
        add(new StringWrapper());
    }
    
    public static Object eval(Object self, Env env) {
        if (self instanceof Evalable)
            return ((Evalable)self).eval(env);
        return self;
    }

    public static Applicable applicable(Symbol method, Object self) {
        Class<?> cls = self.getClass();
        for (Wrapper w = map.get(cls); w != null; w = map.get(w.parentClass())) {
            Applicable r = w.applicable(method, self);
            if (r != null)
                return r;
        }
        return objectWrapper.applicable(method, self);
    }

    public static String print(Object self) {
        if (self == null)
            return "null";
        Class<?> cls = self.getClass();
        for (Wrapper w = map.get(cls); w != null; w = map.get(w.parentClass())) {
            String r = w.print(self);
            if (r != null)
                return r;
        }
        return self.toString();
    }

}

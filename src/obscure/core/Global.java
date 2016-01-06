package obscure.core;

import java.util.HashMap;
import java.util.Map;

import static obscure.core.ListHelper.*;
import obscure.wrappers.IntegerWrapper;
import obscure.wrappers.ObjectWrapper;
import obscure.wrappers.PairWrapper;
import obscure.wrappers.StringWrapper;

public class Global {

    public static final Env ENV = Env.create(null);
    static {
        ENV.define(Symbol.QUOTE, (Applicable)(self, args, env) -> car(args));
        ENV.define(Symbol.of("cons"), (Procedure)(self, args) -> 
            cons(car(args), cadr(args))  );
        ENV.define(Symbol.of("define"), (Applicable)(self, args, env) -> {
            Object first = car(args);
            if (first instanceof Symbol)
                return env.define((Symbol)first, Global.eval(cadr(args), env));
            else if (first instanceof Pair)
                return env.define((Symbol)car(first), new Closure(cdr(first), (List)cdr(args), env));
            else
                throw new ObscureException("cannot define %s", args);
        });
        ENV.define(Symbol.of("Class"), Class.class);
        ENV.define(Symbol.of(";"), (Macro)(args) -> {
            Object w = Nil.value;
            for (Object e : args)
                if (w == Nil.value)
                    w = e;
                else
                    w = cons(car(e), cons(w, cdr(e)));
            return w;
        });
        ENV.define(Symbol.of("lambda"), (Applicable)(self, args, env) ->
            new Closure(car(args), (List)cdr(args), env)
        );
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

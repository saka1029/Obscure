package obscure.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import obscure.globals.*;
import static obscure.core.Helper.*;
import obscure.wrappers.*;

public class Global {

    public static final Env ENV = Env.create(null);
    static {
        ENV.define(Symbol.of(";"), new Cascade());
        ENV.define(Symbol.of("+"), (Macro)(args, env) -> cons(car(args), cons(Symbol.of("+"), cdr(args))));
        ENV.define(Symbol.of("=="), (Procedure)(args, env) -> Objects.equals(car(args), cadr(args)));
        ENV.define(Symbol.of("*"), new Multiply());
        ENV.define(Symbol.of("Class"), Class.class);
        ENV.define(Symbol.of("car"), (Procedure)(self, args) -> car(car(args)));
        ENV.define(Symbol.of("cons"), (Procedure)(self, args) -> cons(car(args), cadr(args)));
        ENV.define(Symbol.of("define"), new Define());
        ENV.define(Symbol.of("define-macro"), new DefineMacro());
        ENV.define(Symbol.of("expand"), new Expand());
        ENV.define(Symbol.LAMBDA, (Applicable)(self, args, env) -> Closure.of(car(args), (List)cdr(args), env));
        ENV.define(Symbol.of("let"), new Let());
        ENV.define(Symbol.of("macro"), new MakeMacro());
        ENV.define(Symbol.QUOTE, (Applicable)(self, args, env) -> car(args));
        ENV.define(Symbol.of("set"), (Applicable)(self, args, env) -> env.set((Symbol)car(args), eval(cadr(args), env)));
    }

    static final Map<Class<?>, Wrapper> map = new HashMap<>();

    static void add(Wrapper w) {
        map.put(w.wrapClass(), w);
    }
    
    static final Wrapper objectWrapper = new ObjectWrapper();

    static {
        add(new PairWrapper());
        add(new IntegerWrapper());
        add(new LongWrapper());
        add(new StringWrapper());
        add(new CharacterWrapper());
    }
    
    public static Object eval(Object self, Env env) {
        if (self instanceof Evalable)
            return ((Evalable)self).eval(env);
        return self;
    }

    public static Applicable applicable(Object self, Object args) {
        Class<?> cls = self.getClass();
        Wrapper w = map.get(cls);
        if (w != null) {
            Applicable a = w.applicable(self, args);
            if (a != null)
                return a;
        }
        return objectWrapper.applicable(self, args);
    }

    public static String print(Object self) {
        if (self == null)
            return "null";
        Class<?> cls = self.getClass();
        Wrapper w = map.get(cls);
        if (w !=  null) {
            String r = w.print(self);
            if (r != null)
                return r;
        }
        return self.toString();
    }

}

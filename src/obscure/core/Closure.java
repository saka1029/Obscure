package obscure.core;

import static obscure.core.ListHelper.*;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntUnaryOperator;
import java.util.function.LongUnaryOperator;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Closure implements Procedure,
        Function<Object, Object>, Predicate<Object>,
        Consumer<Object>, Supplier<Object>,
        IntUnaryOperator, LongUnaryOperator {
 
    final Object parms;
    final List body;
    final Env env;

    public Closure(Object parms, List body, Env env) {
        this.parms = parms;
        this.body = body;
        this.env = env;
    }
    
    static Env pairlis(Object parms, List args, Env env) {
        Env n = Env.create(env);
        Object p = parms;
        Object a = args;
        for (; isPair(p); p = cdr(p), a = cdr(a))
            n.define((Symbol)car(p), car(a));
        if (p instanceof Symbol)
            n.define((Symbol)p, a);
        return n;
    }

    @Override
    public Object apply(Object self, List args) {
        Object r = false;
        Env n = pairlis(parms, args, env);
        for (Object e : body)
            r = eval(e, n);
        return r;
    }
    
    @Override
    public String toString() {
        return print(cons(sym("Closure"), cons(parms, body)));
    }

    @Override
    public boolean test(Object t) {
        return (boolean)apply(null, list(t));
    }

    @Override
    public Object apply(Object t) {
        return apply(null, list(t));
    }

    @Override
    public void accept(Object t) {
        apply(null, list(t));
    }

    @Override
    public Object get() {
        return apply(null, Nil.value);
    }

    @Override
    public int applyAsInt(int operand) {
        return (int)apply(null, list(operand));
    }

    @Override
    public long applyAsLong(long operand) {
        return (long)apply(null, list(operand));
    }

}
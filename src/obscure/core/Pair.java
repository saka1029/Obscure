package obscure.core;

import java.util.Objects;
import static obscure.core.Helper.*;

public class Pair extends List {

    Object car;
    Object cdr;
    
    private Pair(Object car, Object cdr) {
        this.car = car;
        this.cdr = cdr;
    }
    
    @Override
    public Object eval(Env env) {
        Object first = Global.eval(car, env);
        if (first instanceof Applicable)
            return ((Applicable)first).apply(null, asList(cdr), env);
        if (first == null)
            throw new ObscureException("cannot eval %s is null in %s", car, print(this));
        Applicable applicable = Global.applicable(first, cdr);
        List args = isPair(cdr) ? asList(asPair(cdr).cdr) : null;
        return applicable.apply(first, args, env);
    }

    public static Pair of(Object car, Object cdr) {
        return new Pair(car, cdr);
    }

    public static List list(Object... args) {
        Builder b = new Builder();
        for (Object e : args)
            b.tail(e);
        return b.build();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Pair))
            return false;
        Pair o = (Pair)obj;
        return Objects.equals(car, o.car) && Objects.equals(cdr, o.cdr);
    }
    
    @Override
    public String toString() {
        if (car == Symbol.QUOTE && isPair(cdr) && asPair(cdr).cdr == Nil.value)
            return "'" + Global.print(asPair(cdr).car);
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        Object e = this;
        String sep = "";
        for (; isPair(e); e = asPair(e).cdr) {
            sb.append(sep).append(Global.print(asPair(e).car));
            sep = " ";
        }
        if (e != Nil.value)
            sb.append(" . ").append(Global.print(e));
        sb.append(")");
        return sb.toString();
    }
    
    public static class Builder {

        List head = Nil.value;
        List tail = Nil.value;

        public Builder head(Object e) {
            if (head == Nil.value)
                head = tail = new Pair(e, Nil.value);
            else
                head = new Pair(e, head);
            return this;
        }
        
        public Builder tail(Object e) {
            Pair n = new Pair(e, Nil.value);
            if (tail == Nil.value)
                head = tail = n;
            else {
                asPair(tail).cdr = n;
                tail = n;
            }
            return this;
        }
        
        public boolean isEmpty() {
            return tail == Nil.value;
        }

        public Builder end(Object e) {
            if (tail == Nil.value)
                throw new ObscureException("cannot change end to %s", e);
            asPair(tail).cdr = e;
            return this;
        }
        
        public List build() {
            List r = head;
            head = tail = null;
            return r;
        }
    }

    @Override public Object car() { return car; }
    @Override public Object cdr() { return cdr; }

}

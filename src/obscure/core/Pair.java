package obscure.core;

public class Pair implements List {

    Object car;
    Object cdr;
    
    private Pair(Object car, Object cdr) {
        this.car = car;
        this.cdr = cdr;
    }
    
    public static boolean isPair(Object o) { return o instanceof Pair; }
    public static Pair asPair(Object o) { return ((Pair)o); }
    
    public static Pair of(Object car, Object cdr) {
        return new Pair(car, cdr);
    }

    static List list(Object... args) {
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
        return o.car.equals(car) && o.cdr.equals(cdr);
    }
    
    @Override
    public String toString() {
        if (car == Symbol.QUOTE && isPair(cdr) && asPair(cdr).cdr == Nil.value)
            return "'" + asPair(cdr).car;
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        Object e = this;
        String sep = "";
        for (; isPair(e); e = asPair(e).cdr) {
            sb.append(sep).append(asPair(e).car);
            sep = " ";
        }
        if (e != Nil.value)
            sb.append(" . ").append(e);
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
        
        public List tail() {
            return tail;
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
    @Override public boolean isPair() { return true; }

    public static List asList(Object object) {
        return (List)object;
    }

    static Env ENV = Env.create();
    static {
        ENV.define(Symbol.of("car"), (Procedure)((self, args) -> asList(self).car()));
        ENV.define(Symbol.of("cdr"), (Procedure)((self, args) -> asList(self).car()));
    }

    @Override public Object get(Symbol key) {
        return ENV.get(key);
    }

    @Override
    public Object set(Symbol key, Object value) {
        return null;
    }

    @Override
    public Object define(Symbol key, Object value) {
        return null;
    }

    @Override
    public Object eval(Env env) {
        Object first = Global.eval(car, env);
        if (first instanceof Applicable)
            return ((Applicable)first).apply(env, asList(cdr), env);
        if (!(cdr instanceof Pair))
            throw new ObscureException("cannot eval %s", this);
        Pair cdr = (Pair)this.cdr;
        Object second = Global.eval(cdr.car, env);
        Env self;
        if (second instanceof Env)
            self = (Env)second;
        else
            self = Global.wrap(second);
        first = Global.eval(car, self);
        if (!(first instanceof Applicable))
            throw new ObscureException("cannot apply %s %s", first, cdr.cdr);
        return ((Applicable)first).apply(second, asList(cdr.cdr), env);
    }
}

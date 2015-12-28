package obscure.core;

public class Pair extends Obj {

    private Obj car, cdr;
    
    private Pair(Obj car, Obj cdr) {
        this.car = car;
        this.cdr = cdr;
    }
    
    @Override public Obj car() { return car; }
    @Override public Obj cdr() { return cdr; }
    @Override public boolean isPair() { return true; }
    
    public static Pair of(Obj car, Obj cdr) {
        return new Pair(car, cdr);
    }

    static Obj list(Obj... args) {
        Builder b = new Builder();
        for (Obj e : args)
            b.tail(e);
        return b.build();
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    @Override
    public Obj eval(Env env) {
        return car.eval(env).apply(cdr, env);
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
        if (car == Symbol.QUOTE && cdr.isPair() && cdr.cdr() == Nil.value)
            return "'" + cdr.car();
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        Obj e = this;
        String sep = "";
        for (; e.isPair(); e = e.cdr()) {
            sb.append(sep).append(e.car());
            sep = " ";
        }
        if (e != Nil.value)
            sb.append(" . ").append(e);
        sb.append(")");
        return sb.toString();
    }
    
    public static class Builder {

        Obj head = Nil.value;
        Obj tail = Nil.value;

        public Builder head(Obj e) {
            if (head == Nil.value)
                head = tail = new Pair(e, Nil.value);
            else
                head = new Pair(e, head);
            return this;
        }
        
        public Builder tail(Obj e) {
            if (tail == Nil.value)
                head = tail = new Pair(e, Nil.value);
            else
                tail = ((Pair)tail).cdr = new Pair(e, Nil.value);
            return this;
        }
        
        public Obj tail() {
            return tail;
        }

        public Builder end(Obj e) {
            if (tail == Nil.value)
                throw new ObscureException("cannot change end to %s", e);
            ((Pair)tail).cdr = e;
            return this;
        }
        
        public Obj build() {
            Obj r = head;
            head = tail = null;
            return r;
        }

    }
}

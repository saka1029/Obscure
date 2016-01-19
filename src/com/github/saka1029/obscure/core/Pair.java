package com.github.saka1029.obscure.core;

public class Pair extends List {

    Object car, cdr;
    
    Pair(Object car, Object cdr) {
        this.car = car;
        this.cdr = cdr;
    }

    @Override
    public Object eval(Env env) {
        Object first = Global.eval(car, env);
        if (first instanceof Applicable)
            return ((Applicable)first).apply(null, (List)cdr, env);
        if (!(cdr instanceof List))
            throw new ObscureException("cannot eval %s", this);
        for (Object e : (List)cdr)
            first = Global.invoke(e, first, env);
        return first;
    }

    @Override
    public Object invoke(Object self, Env env) {
        if (!(car instanceof Symbol))
            throw new ObscureException("cannto eval %s", this);
        Symbol name = (Symbol)car;
        Applicable method;
        Object value = null;
        Env classEnv = Global.CLASS_ENV.get(self.getClass());
        if (classEnv != null)
            value = classEnv.get(name);
        if (value instanceof Applicable)
            method = (Applicable)value;
        else if (name == Symbol.of("new"))
            method = (Procedure)(s, args) -> Reflection.constructor(s, args.toArray());
        else
            method = (Procedure)(s, args) -> Reflection.method(s, name.name, args.toArray());
        Object result = method.apply(self, (List)cdr, env);
        if (result == Reflection.NOT_FOUND)
            throw new ObscureException("%s does not have method %s", self, name);
        return result;
    }
    
    public static Pair of(Object car, Object cdr) {
        return new Pair(car, cdr);
    }
    
    public static List list(Object... objects) {
        List r = Nil.VALUE;
        for (int i = objects.length - 1; i >= 0; --i)
            r = of(objects[i], r);
        return r;
    }

    @Override
    public Object car() {
        return car;
    }

    @Override
    public Object cdr() {
        return cdr;
    }

    @Override
    public String toString() {
        if (car == Symbol.QUOTE && cdr instanceof Pair && ((Pair)cdr).cdr == Nil.VALUE)
            return "'" + Global.print(((Pair)cdr).car);
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        Object e = this;
        String sep = "";
        for (; e instanceof Pair; e = ((Pair)e).cdr) {
            sb.append(sep).append(Global.print(((Pair)e).car));
            sep = " ";
        }
        if (e != Nil.VALUE)
            sb.append(" . ").append(Global.print(e));
        sb.append(")");
        return sb.toString();
    }
    
    public static class Builder {

        List head = Nil.VALUE;
        List tail = Nil.VALUE;

        public Builder head(Object e) {
            if (head == Nil.VALUE)
                head = tail = new Pair(e, Nil.VALUE);
            else
                head = new Pair(e, head);
            return this;
        }
        
        public Builder tail(Object e) {
            Pair n = new Pair(e, Nil.VALUE);
            if (tail == Nil.VALUE)
                head = tail = n;
            else {
                ((Pair)tail).cdr = n;
                tail = n;
            }
            return this;
        }
        
        public boolean isEmpty() {
            return tail == Nil.VALUE;
        }

        public Builder end(Object e) {
            if (tail == Nil.VALUE)
                throw new ObscureException("cannot change end to %s", e);
            ((Pair)tail).cdr = e;
            return this;
        }
        
        public List build() {
            List r = head;
            head = tail = null;
            return r;
        }
    }
    
    public static Builder builder() {
        return new Builder();
    }
}
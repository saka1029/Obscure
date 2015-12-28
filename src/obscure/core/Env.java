package obscure.core;

import java.util.HashMap;
import java.util.Map;

import obscure.primitive.*;
import obscure.syntax.*;
import obscure.syntax.Class;

public abstract class Env {

    public static final Env GLOBAL = create();
    
    static {
        GLOBAL.put(Symbol.of("car"), new Car());
        GLOBAL.put(Symbol.of("cdr"), new Cdr());
        GLOBAL.put(Symbol.of("cons"), new Cons());
        GLOBAL.put(Symbol.of("equals"), new Equals());
        GLOBAL.put(Symbol.of("list"), new List());
        GLOBAL.put(Symbol.of("class"), new Class());
        GLOBAL.put(Symbol.of("define"), new Define());
        GLOBAL.put(Symbol.of("lambda"), new Lambda());
        GLOBAL.put(Symbol.of("quote"), new Quote());
        GLOBAL.put(Symbol.of("set"), new Set());
    }
    
    public static Env create(Env previous) {
        return new Mapped(previous);
    }

    public static Env create() {
        return create(null);
    }
    
    public static Env coumpound(Env first, Env second) {
        return new Compound(first, second);
    }

    protected abstract Obj get(Symbol key);
    protected abstract void put(Symbol key, Obj value);
    
    public final Obj refer(Symbol key) {
        Obj value = get(key);
        if (value == null)
            value = GLOBAL.get(key);
        if (value == null)
            throw new ObscureException("Env: cannot get %s", key);
        return value;
    }
    
    public final Obj define(Symbol key, Obj value) {
        put(key, value);
        return value;
    }
    
    public final Obj replace(Symbol key, Obj value) {
        Obj obj = get(key);
        if (obj != null)
            put(key, value);
        else {
            obj = GLOBAL.get(key);
            if (obj != null)
                GLOBAL.put(key, value);
            else
                throw new ObscureException("Env: %s not found", key);
        }
        return value;
    }

}

class Mapped extends Env {

    private final Map<Symbol, Obj> map = new HashMap<>();
    private final Env previous;
    
    Mapped(Env previous) {
        this.previous = previous;
    }
    
    public Mapped() {
        this(null);
    }

    @Override
    protected Obj get(Symbol key) {
        Obj value = map.get(key);
        if (value == null && previous != null)
            value = previous.get(key);
        return value;
    }
    
    @Override
    protected void put(Symbol key, Obj value) {
        map.put(key, value);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(map);
        if (previous != null)
            sb.append(" -> ").append(previous);
        return sb.toString();
    }

}

class Compound extends Env {

    final Env first, second;
    
    Compound(Env first, Env second) {
        this.first = first;
        this.second = second;
    }

    @Override
    protected Obj get(Symbol key) {
        Obj r = first.get(key);
        if (r == null)
            r = second.get(key);
        return r;
    }
    
    @Override
    protected void put(Symbol key, Obj value) {
        first.put(key, value);
    }
    
    @Override
    public String toString() {
        return first.toString() + second;
    }

}
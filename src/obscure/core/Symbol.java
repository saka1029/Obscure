package obscure.core;

import java.util.HashMap;
import java.util.Map;

public class Symbol extends Obj {
    
    static final Map<String, Symbol> map = new HashMap<>();
    static final Symbol QUOTE = of("quote");
    static final Symbol TRUE = of("true");
    static final Symbol FALSE = of("false");
    
    final String name;
    
    private Symbol(String name) {
        this.name = name;
        map.put(name, this);
    }
    
    public static Symbol of(String name) {
        Symbol r = map.get(name);
        if (r == null)
            r = new Symbol(name);
        return r;
    }

    public boolean isSymbol() { return true; }
    
    @Override
    public Obj eval(Env env) {
        return env.refer(this);
    }

    @Override
    public String toString() {
        return name;
    }
}

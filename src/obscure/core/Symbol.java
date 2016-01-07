package obscure.core;

import java.util.HashMap;
import java.util.Map;

public class Symbol implements Evalable {
    
    static final Map<String, Symbol> map = new HashMap<>();
    public static final Symbol LAMBDA = of("*lambda");
    public static final Symbol QUOTE = of("quote");
    public static final Symbol TRUE = of("true");
    public static final Symbol FALSE = of("false");
    
    public final String name;
    
    private Symbol(String name) {
        this.name = name;
        map.put(name, this);
    }
    
    public static Symbol of(String name) {
        Symbol r = map.get(name);
        if (r != null)
            return r;
        else
            return new Symbol(name);
    }

    public boolean isSymbol() { return true; }
    
    @Override
    public Object eval(Env env) {
        Object value = env.get(this);
//        if (value == null)
//            System.err.printf("symbol %s not found in %s%n", this, env);
        return value;
    }

    @Override
    public String toString() {
        return name;
    }
}

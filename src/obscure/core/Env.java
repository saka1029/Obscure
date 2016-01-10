package obscure.core;

import java.util.HashMap;
import java.util.Map;

public interface Env {

    Object get(Symbol key);
    Object set(Symbol key, Object value);
    Object define(Symbol key, Object value);

    public static Env create() {
        return new MapEnv(Global.ENV);
    }

    public static Env create(Env parent) {
        return new MapEnv(parent);
    }

}

class MapEnv implements Env {

    final Map<Symbol, Object> map = new HashMap<>();
    final Env parent;
    
    MapEnv(Env parent) {
        this.parent = parent;
    }
    
    @Override
    public Object get(Symbol key) {
        Object value = map.get(key);
        if (value != null)
            return value;
        else if (parent != null)
            return parent.get(key);
        else
            return null;
    }

    @Override
    public Object set(Symbol key, Object value) {
        if (map.containsKey(key)) {
            map.put(key, value);
            return value;
        } else if (parent != null)
            return parent.set(key, value);
        else
            throw new ObscureException("Symbol(%s) not found", key);
    }

    @Override
    public Object define(Symbol key, Object value) {
        map.put(key, value);
        return value;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(map);
        if (parent != null)
            sb.append("->").append(parent);
        return sb.toString();
    }

}
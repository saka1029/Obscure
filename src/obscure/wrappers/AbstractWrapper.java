package obscure.wrappers;

import java.util.HashMap;
import java.util.Map;

import obscure.core.Applicable;
import obscure.core.Symbol;
import obscure.core.Wrapper;

public abstract class AbstractWrapper implements Wrapper {

    protected final Map<Symbol, Applicable> map = new HashMap<>();

    @Override
    public Applicable applicable(Symbol method, Object self) {
        return map.get(method);
    }

    @Override
    public String print(Object self) {
        return null;
    }

}

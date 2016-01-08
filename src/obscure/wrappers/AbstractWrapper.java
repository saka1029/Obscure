package obscure.wrappers;

import java.util.HashMap;
import java.util.Map;

import obscure.core.Applicable;
import static obscure.core.Helper.*;
import obscure.core.Symbol;
import obscure.core.Wrapper;

public abstract class AbstractWrapper implements Wrapper {

    protected final Map<Symbol, Applicable> map = new HashMap<>();

    @Override
    public Applicable applicable(Object self, Object args) {
        return map.get(((Symbol)car(args)));
    }

    @Override
    public String print(Object self) {
        return null;
    }

}

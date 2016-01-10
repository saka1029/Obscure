package obscure.wrappers;

import static obscure.core.Helper.*;

import obscure.core.Applicable;
import obscure.core.Symbol;
import obscure.core.Wrapper;

public class ObjectWrapper implements Wrapper {

    @Override
    public Class<?> wrapClass() {
        return Object.class;
    }

    @Override
    public Applicable applicable(Object self, Object args) {
        if (isPair(args)) {
            String name = ((Symbol)car(args)).name;
            switch (name) {
                case "new" : return new ConstructorProcedure();
                default: return new MethodProcedure(name);
            }
        } else
            return new FieldApplicable(((Symbol)args).name);
    }
    
    @Override
    public String print(Object self) {
        if (self == null)
            return "null";
        return self.toString();
    }

}

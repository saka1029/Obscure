package obscure.primitive;

import obscure.core.Obj;
import obscure.core.Procedure;

public class Cdr extends Procedure {

    @Override
    public Obj apply(Obj args) {
        return args.car().cdr();
    }

}

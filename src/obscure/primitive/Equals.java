package obscure.primitive;

import obscure.core.Obj;
import obscure.core.Procedure;

public class Equals extends Procedure {

    @Override
    public Obj apply(Obj args) {
        return bool(args.car().equals(args.cdr().car()));
    }

}

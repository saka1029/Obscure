package obscure.primitive;

import obscure.core.Obj;
import obscure.core.Pair;
import obscure.core.Procedure;

public class Cons extends Procedure {

    @Override
    public Obj apply(Obj args) {
        return Pair.of(args.car(), args.cdr().car());
    }

}

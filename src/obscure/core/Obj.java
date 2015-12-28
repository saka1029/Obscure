package obscure.core;

public abstract class Obj {

    public boolean isSymbol() { return false; }
    public boolean isPair() { return false; }

    public Obj car() { throw new ObscureException("cannot get car of %s", this); }

    public Obj cdr() { throw new ObscureException("cannot get car of %s", this); }
    
    public Obj eval(Env env) { throw new ObscureException("cannot eval %s", this); }

    public Obj apply(Obj args, Env env) { throw new ObscureException("cannot apply %s %s", this, args); }
    
    public static Obj bool(boolean b) { return b ? Symbol.TRUE : Symbol.FALSE; }

}

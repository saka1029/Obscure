package obscure.core;

public class Nil extends List {

    public static Nil value = new Nil();
    
    private Nil() {
    }
    
    @Override
    public String toString() {
        return "()";
    }

    @Override public Object car() { throw new ObscureException("cannot get car for ()"); }

    @Override public Object cdr() { throw new ObscureException("cannot get cdr for ()"); }

    @Override public Object eval(Env env) { return this; }

}

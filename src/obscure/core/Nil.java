package obscure.core;

public class Nil implements List {

    public static Nil value = new Nil();
    
    private Nil() {
    }
    
    @Override
    public String toString() {
        return "()";
    }

    @Override public Object car() { throw new ObscureException(); }

    @Override public Object cdr() { throw new ObscureException(); }

    @Override public boolean isPair() { return false; }

    @Override public Object get(Symbol key) { return null; }

    @Override public Object set(Symbol key, Object value) { return null; }

    @Override public Object define(Symbol key, Object value) { return null; }

    @Override public Object eval(Env env) { return this; }

}

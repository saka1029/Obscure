package obscure.core;

public class Nil extends Obj {

    public static Nil value = new Nil();
    
    private Nil() {
    }
    
    @Override
    public String toString() {
        return "()";
    }
}

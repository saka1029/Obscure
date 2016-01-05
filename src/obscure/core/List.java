package obscure.core;

public interface List extends Env, Evalable {

    Object car();
    Object cdr();
    boolean isPair();
    
}

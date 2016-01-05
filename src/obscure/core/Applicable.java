package obscure.core;

public interface Applicable {

    Object apply(Object self, List args, Env env);

}

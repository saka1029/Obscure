package obscure.core;

public interface Wrapper {

    Class<?> wrapClass();
    Class<?> parentClass();
    Applicable applicable(Symbol method, Object self);
    String print(Object self);

}

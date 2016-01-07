package obscure.core;

public interface Wrapper {

    Class<?> wrapClass();
    Class<?> parentClass();
    Applicable applicable(Object self, Object args);
    String print(Object self);

}

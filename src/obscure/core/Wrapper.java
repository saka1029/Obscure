package obscure.core;

public interface Wrapper {

    Class<?> wrapClass();
    Applicable applicable(Object self, Object args);
    String print(Object self);

}

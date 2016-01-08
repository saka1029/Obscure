package obscure.test;

import java.util.function.BiFunction;
import java.util.function.Function;

public class GenericFunction<T, U, R> implements Function<T, R>, BiFunction<T, U, R> {

    @Override
    public R apply(T t, U u) {
        return null;
    }

    @Override
    public R apply(T t) {
        return null;
    }

    @Override
    public <V> GenericFunction<T, U, V>  andThen(Function<? super R, ? extends V> after) {
        return new GenericFunctionAndThen<>(after);
    }

    private class GenericFunctionAndThen<V> extends GenericFunction<T, U, V> {
        private final Function<? super R, ? extends V> after;

        public GenericFunctionAndThen(Function<? super R, ? extends V> after) {
            this.after = after;
        }

        @Override
        public V apply(T t) {
            return after.apply(GenericFunction.this.apply(t));
        }

        @Override
        public V apply(T t, U u) {
            return after.apply(GenericFunction.this.apply(t, u));
        }
    }
}
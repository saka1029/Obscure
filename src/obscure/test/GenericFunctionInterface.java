package obscure.test;

import java.util.function.BiFunction;
import java.util.function.Function;

public interface GenericFunctionInterface<T, U, R>
        extends Function<T, R>, BiFunction<T, U, R> {

    @Override
    default <V> GenericFunctionInterface<T, U, V> andThen(
            Function<? super R, ? extends V> after) {
        return new GenericFunctionInterface<T, U, V>() {
            @Override
            public V apply(final T t, final U u) {
                return after.apply(GenericFunctionInterface.this.apply(t, u));
            }

            @Override
            public V apply(final T t) {
                return after.apply(GenericFunctionInterface.this.apply(t));
            }
        };
    }

}

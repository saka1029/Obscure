package io.github.saka1029.obscure.core;

import static io.github.saka1029.obscure.core.Obscure.*;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.BinaryOperator;
import java.util.function.IntBinaryOperator;
import java.util.function.LongBinaryOperator;

public class Closure2 extends Closure implements
        BiConsumer<Object, Object>,
        BiFunction<Object, Object, Object>,
        BinaryOperator<Object>,
        BiPredicate<Object, Object>,
        IntBinaryOperator, LongBinaryOperator {

    protected Closure2(List<Object> parms, List<Object> body, Environment env) {
        super(parms, body, env);
    }

    @Override
    public long applyAsLong(long left, long right) {
        return (long)apply(null, list(left, right));
    }

    @Override
    public int applyAsInt(int left, int right) {
        return (int)apply(null, list(left, right));
    }

    @Override
    public boolean test(Object t, Object u) {
        return (boolean)apply(null, list(t, u));
    }

    @Override
    public Object apply(Object t, Object u) {
        return apply(null, list(t, u));
    }

    @Override
    public void accept(Object t, Object u) {
        apply(null, list(t, u));
    }

}

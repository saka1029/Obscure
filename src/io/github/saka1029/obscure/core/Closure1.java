package io.github.saka1029.obscure.core;

import static io.github.saka1029.obscure.core.Obscure.*;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntUnaryOperator;
import java.util.function.LongUnaryOperator;
import java.util.function.UnaryOperator;
import java.util.function.Predicate;

public class Closure1 extends Closure implements
        Consumer<Object>,
        Function<Object, Object>,
        Predicate<Object>,
        UnaryOperator<Object>,
        IntUnaryOperator, LongUnaryOperator {

    protected Closure1(List<Object> parms, List<Object> body, Env env) {
        super(parms, body, env);
    }

    @Override
    public long applyAsLong(long operand) {
        return (long)apply(null, list(operand));
    }

    @Override
    public int applyAsInt(int operand) {
        return (int)apply(null, list(operand));
    }

    @Override
    public boolean test(Object t) {
        return (boolean)apply(null, list(t));
    }

    @Override
    public Object apply(Object t) {
        return apply(null, list(t));
    }

    @Override
    public void accept(Object t) {
        apply(null, list(t));
    }

}

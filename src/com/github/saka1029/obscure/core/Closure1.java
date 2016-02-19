package com.github.saka1029.obscure.core;

import static com.github.saka1029.obscure.core.Global.*;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;
import java.util.function.LongUnaryOperator;
import java.util.function.UnaryOperator;
import java.util.function.Predicate;

public class Closure1 extends Closure implements
        Consumer<Object>,
        Function<Object, Object>,
        IntFunction<Object>,
        Predicate<Object>,
        IntPredicate,
        UnaryOperator<Object>,
        IntUnaryOperator, LongUnaryOperator {

    protected Closure1(Object parms, List body, Env env) {
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

    @Override
    public boolean test(int value) {
        return (boolean)apply(null, list(value));
    }

    @Override
    public Closure1 negate() {
        return new Closure1(parms, body, env) {
            @Override
            public Object apply(Object self, List args) {
                return !(boolean)super.apply(self, args);
            }
        };
    }

    @Override
    public Object apply(int value) {
        return apply(null, list(value));
    }

}

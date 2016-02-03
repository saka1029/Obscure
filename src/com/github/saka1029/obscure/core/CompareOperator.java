package com.github.saka1029.obscure.core;

import static com.github.saka1029.obscure.core.Global.*;

public class CompareOperator implements Macro {

    final Symbol op;
    
    public CompareOperator(Symbol op) {
        this.op = op;
    }

    /**
     * (< a b) -> (a (compareTo b) (<0))
     */
    @Override
    public Object expand(List args) {
        return list(car(args), list(sym("compareTo"), cadr(args)), list(op));
    }

}

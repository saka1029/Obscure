package com.github.saka1029.obscure.core;

import java.util.Iterator;
import java.util.NoSuchElementException;

public abstract class List implements Evalable, Invokable, Iterable<Object> {

    public abstract Object car();
    public abstract Object cdr();

    public int size() {
        int size = 0;
        for (Object e = this; e instanceof Pair; e = ((Pair)e).cdr)
            ++size;
        return size;
    }

    @Override
    public Iterator<Object> iterator() {
        return new Iterator<Object>() {
            
            List list = List.this;
            
            @Override
            public boolean hasNext() {
                return list instanceof Pair;
            }

            @Override
            public Object next() {
                if (!hasNext())
                    throw new NoSuchElementException();
                Object car = list.car();
                Object cdr = list.cdr();
                list = cdr instanceof List ? (List)cdr : null;
                return car;
            }
            
        };
    }
    
    public Object[] toArray() {
        Object[] r = new Object[size()];
        int i = 0;
        for (Object e : this)
            r[i++] = e;
        return r;
    }
    
}

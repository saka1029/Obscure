package io.github.saka1029.obscure.core;

import java.util.AbstractList;
import java.util.List;

public class ImmutableList extends AbstractList<Object> {

    private final List<Object> list;
    private final int offset;
 
    private ImmutableList(List<Object> list, int offset) {
        this.list= list;
        this.offset = offset;
    }

    @Override
    public Object get(int index) {
        return list.get(index + offset);
    }

    @Override
    public int size() {
        return list.size() - offset;
    }
 
    public static ImmutableList of(List<Object> list) {
        return new ImmutableList(list, 0);
    }
 
    public static ImmutableList cdr(List<Object> list, int n) {
        if (list.size() < n)
            throw new IllegalArgumentException(
                String.format("cannot %s cdr of ()", n));
        return new ImmutableList(list, n);
    }

    public static ImmutableList cdr(List<Object> list) {
        return cdr(list, 1);
    }

}

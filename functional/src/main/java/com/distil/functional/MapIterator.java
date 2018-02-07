package com.distil.functional;

import com.distil.lang.NotNull;

import java.util.Iterator;

public final class MapIterator<F, T> implements Iterator<T> {

    @NotNull
    private final Iterator<F> iterator;
    @NotNull
    private final Unary<F, T> unary;

    @NotNull
    public static <F, T> Iterator<T> mapIterator(
            @NotNull final Iterator<F> iterator,
            @NotNull final Unary<F, T> unary) {
        return new MapIterator<>(iterator, unary);
    }

    private MapIterator(
            @NotNull final Iterator<F> iterator,
            @NotNull final Unary<F, T> unary) {
        this.unary = unary;
        this.iterator = iterator;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public T next() {
        return unary.apply(iterator.next());
    }

    @Override
    public void remove() {
        iterator.remove();
    }
}

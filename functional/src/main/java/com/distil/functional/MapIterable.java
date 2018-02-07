package com.distil.functional;

import com.distil.lang.NotNull;

import java.util.Iterator;

import static com.distil.functional.MapIterator.mapIterator;
import static java.util.Arrays.asList;

public final class MapIterable<F, T> implements Iterable<T> {

    @NotNull
    private final Iterable<F> iterable;
    @NotNull
    private final Unary<F, T> unary;

    @NotNull
    public static <F, T> Iterable<T> mapIterable(
            @NotNull final Unary<F, T> unary,
            @NotNull final Iterable<F> iterable) {
        return new MapIterable<>(iterable, unary);
    }

    @SafeVarargs
    @NotNull
    public static <F, T> Iterable<T> mapArray(
            @NotNull final Unary<F, T> unary,
            @NotNull final F... array) {
        return mapIterable(unary, asList(array));
    }

    private MapIterable(@NotNull final Iterable<F> iterable, @NotNull final Unary<F, T> unary) {
        this.iterable = iterable;
        this.unary = unary;
    }

    @Override
    public Iterator<T> iterator() {
        return mapIterator(iterable.iterator(), unary);
    }
}

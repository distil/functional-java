package com.distil.functional;

import com.distil.lang.NotNull;

import java.util.Iterator;

import static com.distil.functional.JoinIterator.joinIterator;
import static com.distil.functional.MapIterable.mapIterable;
import static java.util.Arrays.asList;

public final class JoinIterable<E> implements Iterable<E> {

    @NotNull
    private static final Unary ITERABLE_ITERATOR =
            (Unary<Iterable<Object>, Iterator<Object>>) Iterable::iterator;

    @NotNull
    private final Iterable<Iterable<E>> iterables;

    @SafeVarargs
    @NotNull
    public static <T> Iterable<T> joinIterable(@NotNull final Iterable<T>... iterables) {
        return joinIterable(asList(iterables));
    }

    @NotNull
    public static <T> Iterable<T> joinIterable(@NotNull final Iterable<Iterable<T>> iterables) {
        return new JoinIterable<>(iterables);
    }

    private JoinIterable(@NotNull final Iterable<Iterable<E>> iterables) {
        this.iterables = iterables;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Iterator<E> iterator() {
        return joinIterator(mapIterable(
                (Unary<Iterable<E>, Iterator<E>>) ITERABLE_ITERATOR, iterables));
    }
}

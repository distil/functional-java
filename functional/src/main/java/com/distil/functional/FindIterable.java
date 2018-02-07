package com.distil.functional;

import com.distil.lang.NotNull;

import java.util.Iterator;

import static com.distil.functional.Option.none;
import static com.distil.functional.Option.some;

public final class FindIterable {

    @NotNull
    public static <T> Option<T> find(
            @NotNull final Iterable<T> iterable,
            @NotNull final Unary<? super T, Boolean> predicate) {
        return find(iterable.iterator(), predicate);
    }

    @NotNull
    public static <T> Option<T> find(
            @NotNull final Iterator<T> iterator,
            @NotNull final Unary<? super T, Boolean> predicate) {
        while (iterator.hasNext()) {
            final T value = iterator.next();
            if (predicate.apply(value)) {
                return some(value);
            }
        }
        return none();
    }

    private FindIterable() {
    }
}

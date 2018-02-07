package com.distil.functional;

import com.distil.lang.NotNull;
import com.distil.lang.Nullable;

import java.util.Iterator;
import java.util.NoSuchElementException;

public final class JoinIterator<E> implements Iterator<E> {

    @NotNull
    private final Iterator<Iterator<E>> iterators;
    @Nullable
    private Iterator<E> iterator;

    @NotNull
    public static <E> Iterator<E> joinIterator(@NotNull final Iterable<Iterator<E>> iterators) {
        return new JoinIterator<>(iterators);
    }

    private JoinIterator(@NotNull final Iterable<Iterator<E>> iterators) {
        this.iterators = iterators.iterator();
    }

    @Override
    public boolean hasNext() {
        if (iterator != null && iterator.hasNext()) {
            return true;
        }

        while (iterators.hasNext()) {
            iterator = iterators.next();
            if (iterator.hasNext()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public E next() {
        if (iterator != null && iterator.hasNext()) {
            return iterator.next();
        } else {
            while (iterators.hasNext()) {
                iterator = iterators.next();
                if (iterator.hasNext()) {
                    return iterator.next();
                }
            }
        }
        throw new NoSuchElementException();
    }

    @Override
    public void remove() {
        if (iterator == null) {
            throw new IllegalStateException();
        } else {
            iterator.remove();
        }
    }
}

package com.distil.functional;

import com.distil.lang.NotNull;
import com.distil.lang.Nullable;

import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static com.distil.functional.Result.err;
import static com.distil.functional.Result.ok;
import static com.distil.lang.Preconditions.checkNotNull;
import static java.util.Collections.singletonList;

public final class Option<T> implements Iterable<T> {

    @NotNull
    private static final Option<Object> NONE = new Option<>(null);
    @SuppressWarnings("ThrowableInstanceNeverThrown")
    @NotNull
    private static final NoSuchElementException NO_SUCH_ELEMENT_EXCEPTION =
            new NoSuchElementException();

    @Nullable
    private final T value;

    @NotNull
    public static <T> Option<T> some(@NotNull final T value) {
        return new Option<>(checkNotNull(value));
    }

    @NotNull
    public static <T> Option<T> someIfNotNull(@Nullable final T value) {
        return value != null ? some(value) : none();
    }

    @SuppressWarnings("unchecked")
    @NotNull
    public static <T> Option<T> none() {
        return (Option<T>) NONE;
    }

    @SuppressWarnings({"unchecked", "unused"})
    @NotNull
    public static <T> Option<T> none(@Nullable final Class<T> clazz) {
        return (Option<T>) NONE;
    }

    private Option(@Nullable final T value) {
        this.value = value;
    }

    public boolean isSome() {
        return value != null;
    }

    public boolean isNone() {
        return value == null;
    }

    @SuppressWarnings("unchecked")
    @NotNull
    public <U> Option<U> map(@NotNull final Unary<? super T, ? extends U> unary) {
        return value != null ? some(unary.apply(value)) : (Option<U>) this;
    }

    @NotNull
    public <E extends Exception> Result<T, E> okOrElse(@NotNull final Supplier<E> err) {
        return value != null ? ok(value) : err(err.get());
    }

    @SuppressWarnings("unchecked")
    @NotNull
    public <U> Option<U> andThen(@NotNull final Unary<? super T, Option<U>> unary) {
        return value != null ? unary.apply(value) : (Option<U>) this;
    }

    @SuppressWarnings("unchecked")
    @NotNull
    public Option<T> or(@NotNull final Option<? extends T> result) {
        return isNone() ? (Option<T>) result : this;
    }

    @NotNull
    public Option<T> orElse(@NotNull final Supplier<Option<T>> supplier) {
        return isSome() ? this : supplier.get();
    }

    @NotNull
    public T unwrapOr(@NotNull final T value) {
        return this.value != null ? this.value : value;
    }

    @NotNull
    public T unwrapOrElse(@NotNull final Supplier<? extends T> supplier) {
        return value != null ? value : supplier.get();
    }

    @Nullable
    public T unwrapOrNull() {
        return value;
    }

    public void sendTo(@NotNull final Receiver<? super T> receiver) {
        if (value != null) {
            receiver.accept(value);
        }
    }

    @NotNull
    public T orThrow() throws NoSuchElementException {
        if (value != null) {
            return value;
        } else {
            throw NO_SUCH_ELEMENT_EXCEPTION;
        }
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return isSome() ? singletonList(value).iterator() : Collections.<T>emptyList().iterator();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Option<?> option = (Option<?>) o;

        return value != null ? value.equals(option.value) : option.value == null;

    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }

    @Override
    public String toString() {
        return isSome() ? "Some{" + value + "}" : "None";
    }
}

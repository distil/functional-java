package com.distil.functional;

import com.distil.lang.NotNull;
import com.distil.lang.Nullable;

import java.util.Collections;
import java.util.Iterator;

import static com.distil.functional.Option.none;
import static com.distil.functional.Option.some;
import static com.distil.lang.Preconditions.checkNotNull;
import static java.util.Collections.singletonList;

public final class Result<T, E extends Exception> implements Iterable<T> {

    @Nullable
    private final T value;
    @Nullable
    private final E exception;

    @NotNull
    public static <T, E extends Exception> Result<T, E> ok(@NotNull final T value) {
        return new Result<>(checkNotNull(value), null);
    }

    @SuppressWarnings("unused")
    @NotNull
    public static <T, E extends Exception> Result<T, E> ok(
            @NotNull final T value, @Nullable final Class<E> exceptionClass) {
        return ok(value);
    }

    @NotNull
    public static <T, E extends Exception> Result<T, E> err(@NotNull final E exception) {
        return new Result<>(null, checkNotNull(exception));
    }

    @SuppressWarnings("unused")
    @NotNull
    public static <T, E extends Exception> Result<T, E> err(
            @Nullable final Class<T> clazz, @NotNull final E exception) {
        return new Result<>(null, checkNotNull(exception));
    }

    @NotNull
    public static <T> Result<T, NullPointerException> okOrNullException(@Nullable final T value) {
        if (value != null) {
            return ok(value);
        } else {
            return err(new NullPointerException());
        }
    }

    private Result(@Nullable final T value, @Nullable final E exception) {
        this.value = value;
        this.exception = exception;
    }

    public boolean isOk() {
        return value != null;
    }

    public boolean isErr() {
        return exception != null;
    }

    @SuppressWarnings("unchecked")
    @NotNull
    public <U> Result<U, E> map(@NotNull final Unary<? super T, U> unary) {
        return value != null ? ok(unary.apply(value)) : (Result<U, E>) this;
    }

    @SuppressWarnings("unchecked")
    @NotNull
    public <F extends Exception> Result<T, F> mapErr(
            @NotNull final Unary<? super E, F> unary) {
        return exception != null ? err(unary.apply(exception)) : (Result<T, F>) this;
    }

    @SuppressWarnings("unchecked")
    @NotNull
    public <U> Result<U, E> andThen(
            @NotNull final Unary<? super T, Result<U, E>> unary) {
        return value != null ? unary.apply(value) : (Result<U, E>) this;
    }

    @SuppressWarnings("unchecked")
    @NotNull
    public Result<T, E> or(@NotNull final Result<? extends T, E> result) {
        return isErr() ? (Result<T, E>) result : this;
    }

    @NotNull
    public Result<T, E> orElse(
            @NotNull final Unary<? super E, Result<T, E>> unary) {
        return exception != null ? unary.apply(exception) : this;
    }

    @NotNull
    public T unwrapOr(@NotNull final T value) {
        return this.value != null ? this.value : value;
    }

    @NotNull
    public T unwrapOrElse(@NotNull final Unary<? super E, ? extends T> unary) {
        return value != null ? value : unary.apply(exception);
    }

    @NotNull
    public Option<T> ok() {
        return value != null ? some(value) : none();
    }

    public void sendTo(@NotNull final Receiver<? super T> receiver) {
        if (value != null) {
            receiver.accept(value);
        }
    }

    public void sendErrTo(@NotNull final Receiver<? super E> receiver) {
        if (exception != null) {
            receiver.accept(exception);
        }
    }

    @NotNull
    public T orThrow() throws E {
        if (value != null) {
            return value;
        } else {
            throw exception;
        }
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return isOk() ? singletonList(value).iterator() : Collections.<T>emptyList().iterator();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Result<?, ?> result = (Result<?, ?>) o;

        if (value != null ? !value.equals(result.value) : result.value != null) return false;
        return exception != null ? exception.equals(result.exception) : result.exception == null;

    }

    @Override
    public int hashCode() {
        int result = value != null ? value.hashCode() : 0;
        result = 31 * result + (exception != null ? exception.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return isOk() ? "Ok{" + value + "}" : "Err{" + exception + "}";
    }
}

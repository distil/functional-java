package com.distil.functional;

import com.distil.lang.NotNull;

import static com.distil.functional.Result.err;
import static com.distil.functional.Result.ok;

public final class ThrowsAsResult {

    @NotNull
    public static <T, E extends Exception> Supplier<Result<T, E>> throwsAsResult(
            @NotNull final ThrowingSupplier<T, E> throwingSupplier,
            @NotNull final Class<E> exceptionClass) {
        return () -> {
            try {
                return ok(throwingSupplier.get());
            } catch (final RuntimeException e) {
                return castRuntimeException(exceptionClass, e);
            } catch (final Exception e) {
                return castException(exceptionClass, e);
            }
        };
    }

    @SuppressWarnings("unchecked")
    @NotNull
    public static <F, T, E extends Exception> Unary<F, Result<T, E>> throwsAsResult(
            @NotNull final ThrowingUnary<F, T, E> throwingUnary,
            @NotNull final Class<E> exceptionClass) {
        return from -> {
            try {
                return ok(throwingUnary.apply(from));
            } catch (final RuntimeException e) {
                return castRuntimeException(exceptionClass, e);
            } catch (final Exception e) {
                return castException(exceptionClass, e);
            }
        };
    }

    @SuppressWarnings("unchecked")
    @NotNull
    public static <A, B, T, E extends Exception> Binary<A, B, Result<T, E>> throwsAsResult(
            @NotNull final ThrowingBinary<A, B, T, E> throwingBinary,
            @NotNull final Class<E> exceptionClass) {
        return (a, b) -> {
            try {
                return ok(throwingBinary.apply(a, b));
            } catch (final RuntimeException e) {
                return castRuntimeException(exceptionClass, e);
            } catch (final Exception e) {
                return castException(exceptionClass, e);
            }
        };
    }

    @SuppressWarnings("unchecked")
    private static <T, E extends Exception> Result<T, E> castException(
            @NotNull final Class<E> exceptionClass, @NotNull final Exception e) {
        if (exceptionClass.isAssignableFrom(e.getClass())) {
            return err((E) e);
        } else {
            /**
             * NOTE:
             * Should never happen since {@link ThrowingSupplier} E type and {@link Class<E>}
             * must match and {@link RuntimeException}s are handled above.
             */
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T, E extends Exception> Result<T, E> castRuntimeException(
            @NotNull final Class<E> exceptionClass, @NotNull final RuntimeException e) {
        if (exceptionClass.isAssignableFrom(e.getClass())) {
            return err((E) e);
        } else {
            throw e;
        }
    }

    private ThrowsAsResult() {
    }
}

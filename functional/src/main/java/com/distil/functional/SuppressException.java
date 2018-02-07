package com.distil.functional;

import com.distil.lang.NotNull;

public final class SuppressException {

    @NotNull
    public static <T, E extends Exception> T suppressException(
            @NotNull final ThrowingSupplier<T, E> throwingSupplier) {
        try {
            return throwingSupplier.get();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    public static <F, T, E extends Exception> T suppressException(
            @NotNull final ThrowingUnary<? super F, T, E> throwingSupplier, @NotNull final F from) {
        try {
            return throwingSupplier.apply(from);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    public static <A, B, T, E extends Exception> T suppressException(
            @NotNull final ThrowingBinary<? super A, ? super B, T, E> throwingSupplier,
            @NotNull final A a,
            @NotNull final B b) {
        try {
            return throwingSupplier.apply(a, b);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

package com.distil.functional;

import com.distil.lang.NotNull;

import static com.distil.functional.Result.ok;

public final class MapErr<F, T, D extends Exception, E extends Exception>
        implements Unary<F, Result<T, E>> {

    @NotNull
    private final Unary<F, Result<T, D>> unary;
    @NotNull
    private final Unary<? super D, E> mapErr;

    @NotNull
    public static <F, T, D extends Exception, E extends Exception> Unary<F, Result<T, E>> mapErr(
            @NotNull final Unary<F, Result<T, D>> unary,
            @NotNull final Unary<? super D, E> mapErrFunction) {
        return new MapErr<>(unary, mapErrFunction);
    }

    private MapErr(@NotNull final Unary<F, Result<T, D>> unary,
                   @NotNull final Unary<? super D, E> mapErr) {
        this.unary = unary;
        this.mapErr = mapErr;
    }

    @NotNull
    @Override
    public Result<T, E> apply(@NotNull final F from) {
        return ok(from, (Class<D>) null)
                .andThen(unary)
                .mapErr(mapErr);
    }
}

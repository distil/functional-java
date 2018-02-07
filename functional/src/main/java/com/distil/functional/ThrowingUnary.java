package com.distil.functional;

import com.distil.lang.NotNull;

public interface ThrowingUnary<F, T, E extends Exception> {

    @NotNull
    T apply(@NotNull F from) throws E;
}

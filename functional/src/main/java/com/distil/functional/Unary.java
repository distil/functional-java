package com.distil.functional;

import com.distil.lang.NotNull;

public interface Unary<F, T> {
    @NotNull
    T apply(@NotNull F from);
}

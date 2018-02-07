package com.distil.functional;

import com.distil.lang.NotNull;

public interface ThrowingSupplier<T, E extends Exception> {

    @NotNull
    T get() throws E;
}

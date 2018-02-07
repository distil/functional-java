package com.distil.functional;

import com.distil.lang.NotNull;

public interface ThrowingBinary<A, B, T, E extends Exception> {

    @NotNull
    T apply(@NotNull A a, @NotNull B b) throws E;
}

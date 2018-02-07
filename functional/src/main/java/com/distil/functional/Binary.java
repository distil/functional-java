package com.distil.functional;

import com.distil.lang.NotNull;

public interface Binary<A, B, R> {
    @NotNull
    R apply(@NotNull A a, @NotNull B b);
}

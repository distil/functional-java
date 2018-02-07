package com.distil.functional;

import com.distil.lang.NotNull;

public interface Supplier<T> {
    @NotNull
    T get();
}

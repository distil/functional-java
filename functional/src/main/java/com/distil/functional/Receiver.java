package com.distil.functional;

import com.distil.lang.NotNull;

public interface Receiver<T> {
    void accept(@NotNull final T value);
}

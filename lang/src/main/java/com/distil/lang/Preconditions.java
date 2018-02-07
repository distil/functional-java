package com.distil.lang;


public final class Preconditions {

    @NotNull
    public static <T> T checkNotNull(@Nullable final T value) {
        if (value == null) {
            throw new NullPointerException();
        } else {
            return value;
        }
    }

    public static void checkArgument(final boolean argument) {
        if (!argument) {
            throw new IllegalArgumentException();
        }
    }

    public static void checkArgument(final boolean argument, @NotNull final String message) {
        if (!argument) {
            throw new IllegalArgumentException(message);
        }
    }
}

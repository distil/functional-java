package com.distil.functional;

import com.distil.lang.NotNull;

import org.junit.Test;

import static com.distil.functional.SuppressException.suppressException;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.fail;

public final class SuppressExceptionTest {

    @NotNull
    private static final String STRING = "Bilbo Baggins";
    @NotNull
    private static final Exception EXCEPTION = new Exception();
    private static final int INTEGER = 111;

    @Test
    public void suppressSupplier() {
        final String string = suppressException(() -> STRING);
        assertThat(string, sameInstance(STRING));

        try {
            suppressException(() -> {
                throw EXCEPTION;
            });
        } catch (final RuntimeException e) {
            assertThat(e.getCause(), sameInstance(EXCEPTION));
            return;
        }
        fail();
    }

    @Test
    public void suppressUnary() {
        final String string = suppressException(from -> from, STRING);
        assertThat(string, sameInstance(STRING));

        try {
            suppressException(from -> {
                throw EXCEPTION;
            }, STRING);
        } catch (final RuntimeException e) {
            assertThat(e.getCause(), sameInstance(EXCEPTION));
            return;
        }
        fail();
    }

    @Test
    public void suppressBinary() {
        final String string = suppressException((a, b) -> a, STRING, INTEGER);
        assertThat(string, sameInstance(STRING));

        try {
            suppressException((a, b) -> {
                throw EXCEPTION;
            }, STRING, INTEGER);
        } catch (final RuntimeException e) {
            assertThat(e.getCause(), sameInstance(EXCEPTION));
            return;
        }
        fail();
    }
}

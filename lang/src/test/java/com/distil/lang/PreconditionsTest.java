package com.distil.lang;

import org.junit.Test;

import static com.distil.lang.Preconditions.checkArgument;
import static com.distil.lang.Preconditions.checkNotNull;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.*;

public final class PreconditionsTest {

    @NotNull
    private static final String STRING = "Bilbo Baggins";

    @Test
    public void checkNotNullWithNull() {
        try {
            checkNotNull(null);
        } catch (final NullPointerException e) {
            return;
        }
        fail();
    }

    @Test
    public void checkNotNullWithNotNull() {
        final String string = checkNotNull(STRING);

        assertThat(string, sameInstance(STRING));
    }

    @Test
    public void checkArgumentFalseWithoutString() {
        try {
            checkArgument(false);
        } catch (final IllegalArgumentException e) {
            return;
        }
        fail();
    }

    @Test
    public void checkArgumentTrueWithoutString() {
        checkArgument(true);
    }

    @Test
    public void checkArgumentFalseWithString() {
        try {
            checkArgument(false, STRING);
        } catch (final IllegalArgumentException e) {
            assertThat(e.getMessage(), sameInstance(STRING));
            return;
        }
        fail();
    }

    @Test
    public void checkArgumentTrueWithString() {
        checkArgument(true, STRING);
    }
}

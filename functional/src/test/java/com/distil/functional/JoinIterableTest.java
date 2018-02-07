package com.distil.functional;

import com.distil.lang.NotNull;

import org.junit.Test;

import static com.distil.functional.JoinIterable.joinIterable;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;

public final class JoinIterableTest {

    @NotNull
    private static final String STRING1 = "Luke";
    @NotNull
    private static final String STRING2 = "Skywalker";
    @NotNull
    private static final String STRING3 = "Darth";
    @NotNull
    private static final String STRING4 = "Vader";

    @Test
    public void iterate() {
        final Iterable<String> strings = joinIterable(
                asList(STRING1, STRING2),
                asList(STRING3, STRING4)
        );
        assertThat(strings, contains(STRING1, STRING2, STRING3, STRING4));
    }
}

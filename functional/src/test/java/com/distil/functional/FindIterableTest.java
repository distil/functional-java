package com.distil.functional;

import com.distil.lang.NotNull;

import org.junit.Test;

import static com.distil.functional.FindIterable.find;
import static com.distil.functional.Option.none;
import static java.util.Arrays.asList;
import static java.util.Collections.emptySet;
import static java.util.Collections.singleton;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.sameInstance;

public final class FindIterableTest {

    @NotNull
    private static final String STRING1 = "Bilbo Baggins";
    @NotNull
    private static final String STRING2 = "Frodo Baggins";

    @Test
    public void empty() {
        assertThat(find(emptySet(), value -> true), sameInstance(none()));
        assertThat(find(emptySet(), value -> false), sameInstance(none()));
    }

    @Test
    public void single() {
        assertThat(find(singleton(STRING1), value -> true).orThrow(), sameInstance(STRING1));
        assertThat(find(singleton(STRING1), value -> false), sameInstance(none()));
    }

    @Test
    public void multiple() {
        assertThat(find(asList(STRING1, STRING2), STRING2::equals).orThrow(), sameInstance(STRING2));
        assertThat(find(asList(STRING1, STRING2), value -> false), sameInstance(none()));
    }
}
package com.distil.functional;

import com.distil.lang.NotNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static com.distil.functional.MapIterable.mapIterable;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public final class MapIterableTest {

    private static final int INTEGER1 = 42;
    private static final int INTEGER2 = 1337;
    @NotNull
    private static final String STRING1 = "Bilbo";
    @NotNull
    private static final String STRING2 = "Baggins";

    @Mock
    private Unary<String, Integer> mockUnary;

    @Before
    public void setUp() {
        initMocks(this);

        when(mockUnary.apply(eq(STRING1))).thenReturn(INTEGER1);
        when(mockUnary.apply(eq(STRING2))).thenReturn(INTEGER2);
    }

    @Test
    public void iterate() {
        final Iterable<Integer> iterable = mapIterable(mockUnary, asList(STRING1, STRING2));

        assertThat(iterable, contains(INTEGER1, INTEGER2));
    }
}

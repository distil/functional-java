package com.distil.functional;

import com.distil.lang.NotNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Iterator;

import static com.distil.functional.MapIterator.mapIterator;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public final class MapIteratorTest {

    @NotNull
    private static final String STRING1 = "Bilbo";
    @NotNull
    private static final String STRING2 = "Baggins";
    private static final int INTEGER1 = 42;
    private static final int INTEGER2 = 1337;

    @Mock
    private Unary<String, Integer> mockUnary;

    @Before
    public void setUp() {
        initMocks(this);
        when(mockUnary.apply(eq(STRING1))).thenReturn(INTEGER1);
        when(mockUnary.apply(eq(STRING2))).thenReturn(INTEGER2);
    }

    @Test
    public void iterator() {
        final Iterator<Integer> iterator = mapIterator(
                asList(STRING1, STRING2).iterator(), mockUnary);
        assertThat(iterator.hasNext(), equalTo(true));
        assertThat(iterator.next(), equalTo(INTEGER1));
        assertThat(iterator.hasNext(), equalTo(true));
        assertThat(iterator.next(), equalTo(INTEGER2));
        assertThat(iterator.hasNext(), equalTo(false));
    }

    @Test
    public void remove() {
        final ArrayList<String> strings = new ArrayList<>(asList(STRING1, STRING2));
        final Iterator<Integer> iterator = mapIterator(strings.iterator(), mockUnary);
        assertThat(iterator.hasNext(), equalTo(true));
        assertThat(iterator.next(), equalTo(INTEGER1));
        iterator.remove();
        assertThat(iterator.next(), equalTo(INTEGER2));
        assertThat(iterator.hasNext(), equalTo(false));
        assertThat(strings.size(), equalTo(1));
        assertThat(strings.get(0), equalTo(STRING2));
    }
}

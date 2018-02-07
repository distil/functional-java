package com.distil.functional;

import com.distil.lang.NotNull;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static com.distil.functional.JoinIterator.joinIterator;
import static java.util.Arrays.asList;
import static java.util.Collections.emptySet;
import static java.util.Collections.singleton;
import static junit.framework.TestCase.fail;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.MockitoAnnotations.initMocks;

public final class JoinIteratorTest {

    @NotNull
    private static final String STRING1 = "Luke";
    @NotNull
    private static final String STRING2 = "Skywalker";
    @NotNull
    private static final String STRING3 = "Darth";
    @NotNull
    private static final String STRING4 = "Vader";

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void iterate() {
        final Iterator<String> iterator = joinIterator(asList(
                asList(STRING1, STRING2).iterator(),
                asList(STRING3, STRING4).iterator()
        ));
        assertThat(iterator.hasNext(), equalTo(true));
        assertThat(iterator.next(), equalTo(STRING1));
        assertThat(iterator.hasNext(), equalTo(true));
        assertThat(iterator.next(), equalTo(STRING2));
        assertThat(iterator.hasNext(), equalTo(true));
        assertThat(iterator.next(), equalTo(STRING3));
        assertThat(iterator.hasNext(), equalTo(true));
        assertThat(iterator.next(), equalTo(STRING4));
        assertThat(iterator.hasNext(), equalTo(false));
    }

    @Test
    public void emptyIteratorArgument() {
        final Iterable<String> emptyIterable = emptySet();
        final Iterator<String> iterator = joinIterator(asList(
                emptyIterable.iterator(),
                asList(STRING1, STRING2).iterator()
        ));
        assertThat(iterator.hasNext(), equalTo(true));
        assertThat(iterator.next(), equalTo(STRING1));
        assertThat(iterator.hasNext(), equalTo(true));
        assertThat(iterator.next(), equalTo(STRING2));
        assertThat(iterator.hasNext(), equalTo(false));
    }

    @Test
    public void emptyIterator() {
        final Iterable<Iterator<String>> emptyIterable = emptySet();
        final Iterator<String> iterator = joinIterator(emptyIterable);
        assertThat(iterator.hasNext(), equalTo(false));
    }

    @Test
    public void nextAfterHasNextFalse() {
        final Iterator<String> iterator = joinIterator(singleton(singleton(STRING1).iterator()));

        assertThat(iterator.hasNext(), equalTo(true));
        assertThat(iterator.next(), equalTo(STRING1));
        assertThat(iterator.hasNext(), equalTo(false));
        try {
            iterator.next();
        } catch (final NoSuchElementException e) {
            return;
        }
        fail();
    }

    @Test
    public void nextWithoutHasNext() {
        final Iterator<String> iterator = joinIterator(asList(
                singleton(STRING1).iterator(),
                singleton(STRING2).iterator()
        ));

        assertThat(iterator.next(), equalTo(STRING1));
        assertThat(iterator.next(), equalTo(STRING2));
        try {
            iterator.next();
        } catch (final NoSuchElementException e) {
            return;
        }
        fail();
    }

    @Test
    public void remove() {
        final ArrayList<String> strings = new ArrayList<>(singleton(STRING1));
        final Iterator<String> iterator = joinIterator(asList(
                strings.iterator(),
                asList(STRING2, STRING3).iterator()
        ));

        assertThat(iterator.hasNext(), equalTo(true));
        assertThat(iterator.next(), equalTo(STRING1));
        iterator.remove();
        assertThat(iterator.hasNext(), equalTo(true));
        assertThat(iterator.next(), equalTo(STRING2));
        assertThat(iterator.hasNext(), equalTo(true));
        assertThat(iterator.next(), equalTo(STRING3));
        assertThat(strings.isEmpty(), equalTo(true));
    }

    @Test
    public void illegalRemoveWithoutNext() {
        final Iterator<String> iterator = joinIterator(singleton(
                singleton(STRING1).iterator()
        ));

        try {
            iterator.remove();
        } catch (final IllegalStateException e) {
            return;
        }
        fail();
    }

    @Test
    public void illegalRemoveOnEmptyIterables() {
        final Iterable<Iterator<String>> emptyIterable = emptySet();
        final Iterator<String> iterator = joinIterator(emptyIterable);

        try {
            iterator.next();
        } catch (final NoSuchElementException ignored) {
        }

        try {
            iterator.remove();
        } catch (final IllegalStateException e) {
            return;
        }
        fail();
    }
}

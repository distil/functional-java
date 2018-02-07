package com.distil.functional;

import com.distil.lang.NotNull;

import org.junit.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static com.distil.functional.Option.none;
import static com.distil.functional.Option.some;
import static com.distil.functional.Option.someIfNotNull;
import static com.distil.functional.Result.err;
import static com.distil.functional.Result.ok;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public final class OptionTest {

    @NotNull
    private static final String STRING = "Frodo Baggins";
    @NotNull
    private static final String STRING2 = "Bilbo Baggins";

    @Test
    public void testSome() {
        final Option<String> option = some(STRING);

        assertThat(option.isSome(), equalTo(true));
        assertThat(option.isNone(), equalTo(false));
    }

    @Test
    public void testNone() {
        final Option<String> none = none();

        assertThat(none.isSome(), equalTo(false));
        assertThat(none.isNone(), equalTo(true));
    }

    @Test
    public void noneWithType() {
        final Option<String> none = none();

        assertThat(none.isSome(), equalTo(false));
        assertThat(none.isNone(), equalTo(true));
    }

    @Test
    public void testSomeIfNotNull() {
        final Option<String> none = someIfNotNull(null);
        final Option<String> some = someIfNotNull(STRING);

        assertThat(none.isSome(), equalTo(false));
        assertThat(none.isNone(), equalTo(true));
        assertThat(some.isSome(), equalTo(true));
        assertThat(some.isNone(), equalTo(false));
    }

    @Test
    public void map() {
        final Option<String> some = some(STRING);
        final Option<Integer> mapOption = some.map(String::length);
        final Option<String> none = none();
        final Option<Integer> noneMapOption =
                none.map(from -> {
                    throw new RuntimeException();
                });

        assertThat(mapOption.orThrow(), equalTo(STRING.length()));
        try {
            noneMapOption.orThrow();
            fail();
        } catch (final NoSuchElementException ignored) {
        }
    }

    @Test
    public void okOr() {
        final NullPointerException nullPointerException = new NullPointerException();
        final Result<String, NullPointerException> okResult =
                some(STRING).okOrElse(() -> nullPointerException);
        final Result<String, NullPointerException> errResult = none(String.class)
                .okOrElse(() -> nullPointerException);

        assertThat(okResult, equalTo(ok(STRING, NullPointerException.class)));
        assertThat(errResult, equalTo(err(String.class, nullPointerException)));
    }

    @Test
    public void andThen() {
        final Unary<String, Option<Integer>> stringToIntegerFunction =
                string -> {
                    try {
                        return some(Integer.valueOf(string));
                    } catch (NumberFormatException e) {
                        return none();
                    }
                };

        final Option<String> some = some("42");
        final Option<String> none = none();

        assertThat(some.andThen(stringToIntegerFunction).orThrow(), equalTo(42));
        try {
            none.andThen((Unary<String, Option<String>>) from -> {
                throw new RuntimeException();
            }).orThrow();
            fail();
        } catch (final NoSuchElementException ignored) {
        }
    }

    @Test
    public void or() throws Exception {
        assertThat(some((CharSequence) STRING).or(some(STRING2)).orThrow(),
                sameInstance(STRING));
        assertThat(some(STRING).or(none()).orThrow(), sameInstance(STRING));
        assertThat(none().or(some(STRING2)).orThrow(),
                sameInstance(STRING2));
        try {
            none().or(none()).orThrow();
            fail();
        } catch (final NoSuchElementException ignored) {
        }
    }

    @Test
    public void orElse() {
        assertThat(some(STRING).orElse(() -> some(STRING2)).orThrow(),
                sameInstance((CharSequence) STRING));
        assertThat(some(STRING).orElse(Option::none).orThrow(), sameInstance(STRING));
        assertThat(none().orElse(() -> some(STRING2)).orThrow(),
                sameInstance(STRING2));
        try {
            none().orElse(Option::none).orThrow();
            fail();
        } catch (final NoSuchElementException ignored) {
        }
    }

    @Test
    public void unwrapOr() {
        assertThat(some(STRING).unwrapOr(STRING2), sameInstance(STRING));
        assertThat(none().unwrapOr(STRING2), sameInstance(STRING2));
    }

    @Test
    public void unwrapOrElse() {
        assertThat(some(STRING).unwrapOrElse(() -> {
            throw new RuntimeException();
        }),
                sameInstance(STRING));
        assertThat(none().unwrapOrElse(() -> STRING), sameInstance(STRING));
    }

    @Test
    public void unwrapOrNull() {
        assertThat(some(STRING).unwrapOrNull(), sameInstance(STRING));
        assertThat(none().unwrapOrNull(), nullValue());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void sendTo() {
        final Receiver<String> mockReceiver = mock(Receiver.class);

        some(STRING).sendTo(mockReceiver);
        none(String.class).sendTo(mockReceiver);

        verify(mockReceiver, times(1)).accept(same(STRING));
    }

    @Test
    public void orThrow() throws Exception {
        assertThat(some(STRING).orThrow(), sameInstance(STRING));
        try {
            none().orThrow();
            fail();
        } catch (final NoSuchElementException ignored) {
        }
    }

    @Test
    public void iterator() {
        assertThat(none().iterator().hasNext(), equalTo(false));

        final Iterator<String> iterator = some(STRING).iterator();
        assertThat(iterator.hasNext(), equalTo(true));
        assertThat(iterator.next(), sameInstance(STRING));
        assertThat(iterator.hasNext(), equalTo(false));
    }

    @Test
    public void equals() {
        assertThat(some(STRING), equalTo(some(STRING + "")));
        assertThat(some(STRING), not(equalTo(some(STRING2))));
        assertThat(none(), equalTo(none()));
    }

    @Test
    public void testToString() {
        assertThat(some(STRING).toString(),
                equalTo("Some{" + STRING + "}"));
        assertThat(none().toString(), equalTo("None"));
    }

    @Test
    public void hash() {
        assertThat(some(STRING).hashCode(), equalTo(STRING.hashCode()));
    }
}

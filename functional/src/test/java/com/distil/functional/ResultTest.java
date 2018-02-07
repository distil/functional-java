package com.distil.functional;

import com.distil.lang.NotNull;

import org.junit.Test;

import java.util.Iterator;

import static com.distil.functional.Option.none;
import static com.distil.functional.Result.err;
import static com.distil.functional.Result.ok;
import static com.distil.functional.Result.okOrNullException;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.sameInstance;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public final class ResultTest {

    @NotNull
    private static final String STRING = "Frodo Baggins";
    @NotNull
    private static final String STRING2 = "Bilbo Baggins";
    @SuppressWarnings("ThrowableInstanceNeverThrown")
    @NotNull
    private static final TestException EXCEPTION = new TestException();
    @SuppressWarnings("ThrowableInstanceNeverThrown")
    @NotNull
    private static final TestException EXCEPTION_2 = new TestException();
    @SuppressWarnings("ThrowableInstanceNeverThrown")
    @NotNull
    private static final NullPointerException NULL_POINTER_EXCEPTION = new NullPointerException();
    private static final int INTEGER = 42;

    private static final class TestException extends Exception {
    }

    @Test
    public void testOk() {
        final Result<String, Exception> result = ok(STRING);
        assertThat(result.isOk(), equalTo(true));
        assertThat(result.isErr(), equalTo(false));
    }

    @Test
    public void okWithExceptionType() {
        final Result<String, NullPointerException> result = ok(STRING, NullPointerException.class);
        assertThat(result.isOk(), equalTo(true));
        assertThat(result.isErr(), equalTo(false));
    }

    @Test
    public void testOkOrNullException() {
        final Result<String, NullPointerException> okResult = okOrNullException(STRING);
        final Result<String, NullPointerException> nullResult = okOrNullException(null);

        assertThat(okResult.isOk(), equalTo(true));
        assertThat(okResult.isErr(), equalTo(false));
        assertThat(nullResult.isOk(), equalTo(false));
        assertThat(nullResult.isErr(), equalTo(true));
    }

    @Test
    public void testErr() {
        final Result<String, TestException> result = err(EXCEPTION);
        assertThat(result.isErr(), equalTo(true));
        assertThat(result.isOk(), equalTo(false));
    }

    @Test
    public void errWithType() {
        final Result<String, TestException> result = err(String.class, EXCEPTION);
        assertThat(result.isErr(), equalTo(true));
        assertThat(result.isOk(), equalTo(false));
    }

    @Test
    public void map() throws TestException {
        final Result<String, TestException> ok = ok(STRING);
        final Result<Integer, TestException> mapResult = ok.map(String::length);
        final Result<String, TestException> err = err(EXCEPTION);
        final Result<Integer, TestException> errMapResult =
                err.map(from -> {
                    throw new RuntimeException();
                });

        assertThat(mapResult, equalTo(ok(STRING.length(), TestException.class)));
        assertThat(errMapResult, equalTo(err(Integer.class, EXCEPTION)));
    }

    @Test
    public void mapErr() {
        final Result<String, TestException> ok = ok(STRING);
        final Result<String, NullPointerException> okMapErr =
                ok.mapErr(from -> {
                    throw new RuntimeException();
                });
        final Result<Object, TestException> err = err(EXCEPTION);
        final Result<Object, NullPointerException> errMapErr =
                err.mapErr(exception -> NULL_POINTER_EXCEPTION);
        assertThat(okMapErr, equalTo(ok(STRING, NullPointerException.class)));
        assertThat(errMapErr, equalTo(err(NULL_POINTER_EXCEPTION)));
    }

    @Test
    public void andThen() {
        final Unary<TestException, NumberFormatException> mapErrFunction =
                exception -> new NumberFormatException(TestException.class.getCanonicalName());
        final Unary<String, Result<Integer, NumberFormatException>> stringToIntegerFunction =
                string -> {
                    try {
                        return ok(Integer.valueOf(string));
                    } catch (NumberFormatException e) {
                        return err(e);
                    }
                };

        final Result<String, TestException> ok = ok(Integer.valueOf(INTEGER).toString());
        final Result<String, TestException> err = err(EXCEPTION);

        final Unary<TestException, NumberFormatException> throwExceptionUnary =
                from -> {
                    throw new RuntimeException();
                };
        assertThat(ok.mapErr(throwExceptionUnary).andThen(stringToIntegerFunction),
                equalTo(ok(INTEGER, NumberFormatException.class)));
        final Unary<String, Result<String, NumberFormatException>> throwExceptionFunction2 =
                from -> {
                    throw new RuntimeException();
                };
        try {
            err.mapErr(mapErrFunction).andThen(throwExceptionFunction2).orThrow();
            fail();
        } catch (final NumberFormatException ignored) {
        }
    }

    @Test
    public void or() throws Exception {
        assertThat(ok((CharSequence) STRING).or(ok(STRING2)),
                equalTo(ok((CharSequence) STRING)));
        assertThat(ok(STRING).or(err(String.class, EXCEPTION)),
                equalTo(ok(STRING)));
        assertThat(err(CharSequence.class, EXCEPTION).or(ok(STRING2, TestException.class)),
                equalTo(ok((CharSequence) STRING2, TestException.class)));
        assertThat(err(String.class, EXCEPTION).or(err(String.class, EXCEPTION_2)),
                equalTo(err(String.class, EXCEPTION_2)));
    }

    @Test
    public void orElse() throws Exception {
        assertThat(ok((CharSequence) STRING)
                        .orElse(exception -> ok(STRING2)),
                equalTo(ok((CharSequence) STRING)));
        assertThat(ok(STRING).or(err(String.class, EXCEPTION)),
                equalTo(ok(STRING)));
        assertThat(err(CharSequence.class, EXCEPTION)
                .orElse(exception -> ok(STRING2)),
                equalTo(ok((CharSequence) STRING2, TestException.class)));
        assertThat(err(String.class, EXCEPTION)
                .orElse(exception -> {
                    assertThat(exception, sameInstance(EXCEPTION));
                    return err(EXCEPTION_2);
                }),
                equalTo(err(String.class, EXCEPTION_2)));
    }

    @Test
    public void unwrapOr() {
        assertThat(ok(STRING).unwrapOr(STRING2), sameInstance(STRING));
        assertThat(err(String.class, EXCEPTION).unwrapOr(STRING2), sameInstance(STRING2));
    }

    @Test
    public void unwrapOrElse() {
        assertThat(ok(STRING).unwrapOrElse(from -> {
            throw new RuntimeException();
        }),
                sameInstance(STRING));
        assertThat(err(String.class, EXCEPTION)
                .unwrapOrElse(exception -> {
                    assertThat(exception, sameInstance(EXCEPTION));
                    return STRING;
                }), sameInstance(STRING));
    }

    @Test
    public void optionOk() {
        assertThat(ok(STRING).ok().orThrow(), sameInstance(STRING));
        assertThat(err(EXCEPTION).ok(), sameInstance(none()));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void sendToSuccess() {
        final Receiver<String> mockReceiver = mock(Receiver.class);

        ok(STRING).sendTo(mockReceiver);

        verify(mockReceiver).accept(same(STRING));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void sendToFailure() {
        final Receiver<String> mockReceiver = mock(Receiver.class);

        err(String.class, (Exception) EXCEPTION).sendTo(mockReceiver);

        verify(mockReceiver, never()).accept(same(STRING));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void sendErrToSuccess() {
        final Receiver<Exception> mockReceiver = mock(Receiver.class);

        err(String.class, EXCEPTION).sendErrTo(mockReceiver);

        verify(mockReceiver).accept(same(EXCEPTION));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void sendErrToFailure() {
        final Receiver<Exception> mockReceiver = mock(Receiver.class);

        ok(STRING, TestException.class).sendErrTo(mockReceiver);

        verify(mockReceiver, never()).accept(same(EXCEPTION));
    }

    @Test
    public void orThrow() throws Exception {
        assertThat(ok(STRING).orThrow(), sameInstance(STRING));
        try {
            err(EXCEPTION).orThrow();
            fail();
        } catch (final TestException e) {
            assertThat(e, sameInstance(EXCEPTION));
        }
    }

    @Test
    public void iterator() {
        assertThat(err(EXCEPTION).iterator().hasNext(), equalTo(false));

        final Iterator<String> iterator = ok(STRING).iterator();
        assertThat(iterator.hasNext(), equalTo(true));
        assertThat(iterator.next(), sameInstance(STRING));
        assertThat(iterator.hasNext(), equalTo(false));
    }

    @Test
    public void equals() {
        assertThat(ok(STRING), equalTo(ok(STRING + "")));
        assertThat(ok(STRING), not(equalTo(ok(STRING2))));
        assertThat(err(EXCEPTION), equalTo(err(EXCEPTION)));
    }

    @Test
    public void hash() {
        final Exception exception = new StaticHashException();
        final Exception exception2 = new StaticHashException();

        assertThat(ok(STRING).hashCode(), equalTo(ok(STRING + "").hashCode()));
        assertThat(err(exception).hashCode(), equalTo(err(exception2).hashCode()));
    }

    @Test
    public void testToString() {
        assertThat(
                ok(STRING).toString(),
                equalTo("Ok{" + STRING + "}"));
        assertThat(
                err(new Exception(STRING)).toString(),
                equalTo("Err{"
                        + Exception.class.getName()
                        + ": "
                        + STRING
                        + "}"));
    }

    private static final class StaticHashException extends Exception {

        @Override
        public int hashCode() {
            return INTEGER;
        }
    }
}

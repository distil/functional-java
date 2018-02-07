package com.distil.functional;

import com.distil.lang.NotNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.NoSuchElementException;

import static com.distil.functional.MapErr.mapErr;
import static com.distil.functional.Result.err;
import static com.distil.functional.Result.ok;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public final class MapErrTest {

    @NotNull
    private static final String STRING = "Darth Vader";
    private static final Integer INTEGER = 42;
    @SuppressWarnings("ThrowableInstanceNeverThrown")
    @NotNull
    private static final NullPointerException NULL_POINTER_EXCEPTION = new NullPointerException();
    @SuppressWarnings("ThrowableInstanceNeverThrown")
    @NotNull
    private static final NoSuchElementException NO_SUCH_ELEMENT_EXCEPTION =
            new NoSuchElementException();

    @Mock
    private Unary<String, Result<Integer, NullPointerException>> mockSuccess;
    @Mock
    private Unary<String, Result<Integer, NullPointerException>> mockFailure;
    @Mock
    private Unary<RuntimeException, NoSuchElementException> mockMapError;

    @Before
    public void setUp() {
        initMocks(this);

        when(mockSuccess.apply(eq(STRING)))
                .thenReturn(ok(INTEGER, NullPointerException.class));
        when(mockFailure.apply(eq(STRING)))
                .thenReturn(err(Integer.class, NULL_POINTER_EXCEPTION));
        when(mockMapError.apply(eq(NULL_POINTER_EXCEPTION)))
                .thenReturn(NO_SUCH_ELEMENT_EXCEPTION);
    }

    @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
    @Test
    public void testSuccess() {
        final Result<Integer, NoSuchElementException> result =
                mapErr(mockSuccess, mockMapError).apply(STRING);

        assertThat(result, equalTo(ok(INTEGER, NoSuchElementException.class)));
        verify(mockSuccess).apply(eq(STRING));
        verify(mockMapError, never()).apply(any(RuntimeException.class));
    }

    @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
    @Test
    public void testFailure() {
        final Result<Integer, NoSuchElementException> result =
                mapErr(mockFailure, mockMapError).apply(STRING);

        assertThat(result,
                equalTo(err(Integer.class, NO_SUCH_ELEMENT_EXCEPTION)));
        verify(mockFailure).apply(eq(STRING));
        verify(mockMapError).apply(eq(NULL_POINTER_EXCEPTION));
    }
}

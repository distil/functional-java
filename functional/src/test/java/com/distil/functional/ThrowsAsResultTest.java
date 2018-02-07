package com.distil.functional;

import com.distil.lang.NotNull;

import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

import static com.distil.functional.ThrowsAsResult.throwsAsResult;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.fail;

public final class ThrowsAsResultTest {

    @NotNull
    private static final String STRING = "Bilbo Baggins";
    @NotNull
    private static final IOException EXCEPTION = new IOException();
    @NotNull
    private static final RuntimeException RUNTIME_EXCEPTION = new RuntimeException();
    @NotNull
    private static final FileNotFoundException FILE_NOT_FOUND_EXCEPTION =
            new FileNotFoundException();
    @NotNull
    private static final IllegalArgumentException ILLEGAL_ARGUMENT_EXCEPTION =
            new IllegalArgumentException();
    private static final int INTEGER = 111;

    @Test
    public void successSupplier() throws IOException {
        final Supplier<Result<String, IOException>> supplier = throwsAsResult(
                () -> STRING,
                IOException.class
        );

        final Result<String, IOException> string = supplier.get();
        assertThat(string.isOk(), equalTo(true));
        string.sendTo(value -> assertThat(value, sameInstance(STRING)));
    }

    @Test
    public void failureSupplier() {
        final Supplier<Result<String, IOException>> supplier = throwsAsResult(
                () -> {
                    throw EXCEPTION;
                },
                IOException.class
        );

        final Result<String, IOException> string = supplier.get();
        assertThat(string.isErr(), equalTo(true));
        string.sendErrTo(value -> assertThat(value, sameInstance(EXCEPTION)));
    }

    @Test
    public void subclassSupplier() {
        final Supplier<Result<String, IOException>> supplier = throwsAsResult(
                () -> {
                    throw FILE_NOT_FOUND_EXCEPTION;
                },
                IOException.class
        );

        final Result<String, IOException> string = supplier.get();
        assertThat(string.isErr(), equalTo(true));
        string.sendErrTo(value -> assertThat(value, sameInstance(FILE_NOT_FOUND_EXCEPTION)));
    }

    @Test
    public void runtimeExceptionSupplier() {
        final Supplier<Result<String, IOException>> supplier = throwsAsResult(
                () -> {
                    throw RUNTIME_EXCEPTION;
                },
                IOException.class
        );

        try {
            supplier.get();
        } catch (final RuntimeException e) {
            return;
        }
        fail();
    }

    @Test
    public void typedRuntimeExceptionSupplier() {
        final Supplier<Result<String, IllegalArgumentException>> supplier = throwsAsResult(
                () -> {
                    throw ILLEGAL_ARGUMENT_EXCEPTION;
                },
                IllegalArgumentException.class
        );

        final Result<String, IllegalArgumentException> string = supplier.get();
        assertThat(string.isErr(), equalTo(true));
        string.sendErrTo(value -> assertThat(value, sameInstance(ILLEGAL_ARGUMENT_EXCEPTION)));
    }

    @Test
    public void successUnary() throws IOException {
        final Unary<String, Result<String, IOException>> supplier = throwsAsResult(
                string -> string,
                IOException.class
        );

        final Result<String, IOException> string = supplier.apply(STRING);
        assertThat(string.isOk(), equalTo(true));
        string.sendTo(value -> assertThat(value, sameInstance(STRING)));
    }

    @Test
    public void failureUnary() {
        final Unary<String, Result<String, IOException>> supplier = throwsAsResult(
                string -> {
                    throw EXCEPTION;
                },
                IOException.class
        );

        final Result<String, IOException> string = supplier.apply(STRING);
        assertThat(string.isErr(), equalTo(true));
        string.sendErrTo(value -> assertThat(value, sameInstance(EXCEPTION)));
    }

    @Test
    public void subclassUnary() {
        final Unary<String, Result<String, IOException>> supplier = throwsAsResult(
                string -> {
                    throw FILE_NOT_FOUND_EXCEPTION;
                },
                IOException.class
        );

        final Result<String, IOException> string = supplier.apply(STRING);
        assertThat(string.isErr(), equalTo(true));
        string.sendErrTo(value -> assertThat(value, sameInstance(FILE_NOT_FOUND_EXCEPTION)));
    }

    @Test
    public void runtimeExceptionUnary() {
        final Unary<String, Result<String, IOException>> supplier = throwsAsResult(
                string -> {
                    throw RUNTIME_EXCEPTION;
                },
                IOException.class
        );

        try {
            supplier.apply(STRING);
        } catch (final RuntimeException e) {
            return;
        }
        fail();
    }

    @Test
    public void successBinary() throws IOException {
        final Binary<String, Integer, Result<String, IOException>> supplier = throwsAsResult(
                (string, integer) -> STRING,
                IOException.class
        );

        final Result<String, IOException> string = supplier.apply(STRING, INTEGER);
        assertThat(string.isOk(), equalTo(true));
        string.sendTo(value -> assertThat(value, sameInstance(STRING)));
    }

    @Test
    public void failureBinary() {
        final Binary<String, Integer, Result<String, IOException>> supplier = throwsAsResult(
                (a, b) -> {
                    throw EXCEPTION;
                },
                IOException.class
        );

        final Result<String, IOException> string = supplier.apply(STRING, INTEGER);
        assertThat(string.isErr(), equalTo(true));
        string.sendErrTo(value -> assertThat(value, sameInstance(EXCEPTION)));
    }

    @Test
    public void subclassBinary() {
        final Binary<String, Integer, Result<String, IOException>> supplier = throwsAsResult(
                (a, b) -> {
                    throw FILE_NOT_FOUND_EXCEPTION;
                },
                IOException.class
        );

        final Result<String, IOException> string = supplier.apply(STRING, INTEGER);
        assertThat(string.isErr(), equalTo(true));
        string.sendErrTo(value -> assertThat(value, sameInstance(FILE_NOT_FOUND_EXCEPTION)));
    }

    @Test
    public void runtimeExceptionBinary() {
        final Binary<String, Integer, Result<String, IOException>> supplier = throwsAsResult(
                (a, b) -> {
                    throw RUNTIME_EXCEPTION;
                },
                IOException.class
        );

        try {
            supplier.apply(STRING, INTEGER);
        } catch (final RuntimeException e) {
            return;
        }
        fail();
    }
}
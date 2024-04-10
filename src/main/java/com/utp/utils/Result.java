package com.utp.utils;

import java.util.Optional;
import java.util.function.Function;

public class Result<T, E> {

    private Optional<T> value;
    private Optional<E> error;

    private Result(T value, E error) {
        this.value = Optional.ofNullable(value);
        this.error = Optional.ofNullable(error);
    }

    public static <U, E> Result<U, E> ok(U value) {
        return new Result<>(value, null);
    }

    public static <U, E> Result<U, E> error(E error) {
        return new Result<>(null, error);
    }

    public boolean isError() {
        return error.isPresent();
    }

    public boolean isOk() {
        return value.isPresent();
    }

    public T unwrapOk() throws RuntimeException {
        if (this.isError())
            throw new RuntimeException("Called unwrapOk on an error Result");
        return value.get();
    }

    public E unwrapError() throws RuntimeException {
        if (this.isOk())
            throw new RuntimeException("Called unwrapError on an ok Result");
        return error.get();
    }

    public <U> Result<U, E> flatMap(Function<T, Result<U,E>> mapper) {

        if (this.isError()) {
            return Result.error(error.get());
        }

        return mapper.apply(value.get());
    }
}
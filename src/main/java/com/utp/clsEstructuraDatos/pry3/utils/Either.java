package com.utp.clsEstructuraDatos.pry3.utils;

import java.util.Optional;

public class Either<T, E> {

    private Optional<T> left;
    private Optional<E> right;

    private Either(T value, E error) {
        this.left = Optional.ofNullable(value);
        this.right = Optional.ofNullable(error);
    }

    public static <U, E> Either<U, E> left(U left) {
        return new Either<>(left, null);
    }

    public static <U, E> Either<U, E> right(E right) {
        return new Either<>(null, right);
    }

    public boolean isRight() {
        return right.isPresent();
    }

    public boolean isLeft() {
        return left.isPresent();
    }

    public T get() throws RuntimeException {
        return unwrapLeft();
    }

    public T unwrapLeft() throws RuntimeException {
        if (this.isRight())
            throw new RuntimeException("Called unwrapLeft on an Right Either");
        return left.get();
    }

    public E unwrapRight() throws RuntimeException {
        if (this.isLeft())
            throw new RuntimeException("Called UnwrapRight on an Left Either");
        return right.get();
    }

}

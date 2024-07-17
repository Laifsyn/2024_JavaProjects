package com.utp.clsEstructuraDatos.Estructuras.pila;

public interface IStack<T> {
    default boolean isEmpty() {
        return (len() == 0);
    }

    default boolean isFull() {
        return len() == capacity();
    }

    void push(T elemento);

    T pop();

    T peek_top();

    int len();

    void clear();

    int capacity();
}

package com.utp.clsEstructuraDatos.Estructuras.colas;

public interface IQueue<T> {
    default boolean isEmpty() {
        return (len() == 0);
    }

    boolean isFull();

    void insert(T elemento);

    T remove();

    T peek();

    int len();

    void clear();
}
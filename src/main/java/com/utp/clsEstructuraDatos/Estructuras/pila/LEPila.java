package com.utp.clsEstructuraDatos.Estructuras.pila;

import com.utp.clsEstructuraDatos.Estructuras.linked_list.LinkedList;

public class LEPila<T> implements IStack<T> {
    int len = 0;
    LinkedList<T> inner = new LinkedList<T>();

    @Override
    public void push(T elemento) {
        len += 1;
        inner.insert_first(elemento);
    }

    @Override
    public T pop() {
        if (isEmpty()) {
            return null;
        }
        var e = inner.remove_first();
        len -= 1;
        if (e.isPresent()) {
            return e.get();
        }
        return null;
    }

    @Override
    public T peek_top() {
        var e = inner.get(0);
        if (e.isPresent()) {
            return e.get();
        }
        return null;
    }

    @Override
    public int len() {
        return this.len;
    }

    @Override
    public void clear() {
        inner.clear();
    }

    @Override
    public int capacity() {
        return -1;
    }

    @Override
    /**
     * Linked List has no capacity constraint
     */
    public boolean isFull() {
        return false;
    }

}

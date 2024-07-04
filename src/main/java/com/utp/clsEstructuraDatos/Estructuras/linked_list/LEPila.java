package com.utp.clsEstructuraDatos.Estructuras.linked_list;

import com.utp.clsEstructuraDatos.Estructuras.pila.IStack;

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
        len = 0;
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

    public LinkedList<T> to_inverted() {
        var inner = new LinkedList<T>();
        for (int i = len - 1; i >= 0; i--) {
            inner.insert_first(this.inner.get(i).get());
        }
        return inner;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < len; i++) {
            sb.append(inner.get(i).get().toString());
            sb.append("| ");
        }
        sb.delete(sb.length() - 2, sb.length());
        sb.append("]");
        return sb.toString();
    }
}

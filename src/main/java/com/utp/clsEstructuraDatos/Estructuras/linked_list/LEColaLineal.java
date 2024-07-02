package com.utp.clsEstructuraDatos.Estructuras.linked_list;

import com.utp.clsEstructuraDatos.Estructuras.colas.AbstractLinkedQueue;

public class LEColaLineal<T> extends AbstractLinkedQueue<T> {

    /**
     * Append at the end of the queue
     */
    @Override
    public void insert(T elemento) {
        len += 1;
        inner.insert_last(elemento);
    }

    /**
     * Remove the latest inserted element from the queue
     */
    @Override
    public T remove() {
        if (this.isEmpty()) {
            return null;
        }
        if (inner.len() == 0) {
            len = 0;
        }
        return inner.remove_first().get();
    }

    @Override
    public T peek() {
        var e = inner.get(0);
        if (e.isPresent()) {
            return e.get();
        }
        return null;
    }

    public LinkedList<T> extricate() {
        return inner;
    }
}

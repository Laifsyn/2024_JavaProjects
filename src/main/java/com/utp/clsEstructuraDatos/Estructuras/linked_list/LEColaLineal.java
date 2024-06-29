package com.utp.clsEstructuraDatos.Estructuras.linked_list;

import com.utp.clsEstructuraDatos.Estructuras.colas.AbstractLinkedQueue;

public class LEColaLineal<T> extends AbstractLinkedQueue<T> {

    @Override
    public void insert(T elemento) {
        len += 1;
        inner.insert_last(elemento);
    }

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

}

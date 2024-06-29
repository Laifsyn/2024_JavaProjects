package com.utp.clsEstructuraDatos.Estructuras.colas;

import com.utp.clsEstructuraDatos.Estructuras.linked_list.LinkedList;

public abstract class AbstractLinkedQueue<T> implements IQueue<T> {
    protected LinkedList<T> inner;
    protected int len = 0;

    @Override
    /**
     * Linked List has no capacity constraint
     */
    public boolean isFull() {
        return false;
    }

    @Override
    public int len() {
        return this.len;
    }

    @Override
    public void clear() {
        inner.clear();
        this.len = 0;
    }

}

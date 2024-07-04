package com.utp.clsEstructuraDatos.Estructuras.colas;

import com.utp.clsEstructuraDatos.Estructuras.linked_list.LinkedList;

public abstract class AbstractLinkedQueue<T> implements IQueue<T> {
    protected LinkedList<T> inner = new LinkedList<>();
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

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < inner.len(); i++) {
            sb.append(inner.get(i).get().toString());
            sb.append(", ");
        }
        sb.delete(sb.length() - 2, sb.length());
        sb.append("]");
        return sb.toString();
    }
}

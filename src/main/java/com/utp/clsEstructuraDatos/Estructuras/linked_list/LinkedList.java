package com.utp.clsEstructuraDatos.Estructuras.linked_list;

import java.util.Optional;

public class LinkedList<T> {
    Optional<Node<T>> child;
    int len = 0;

    public void insert_last(T element) {
        if (len == 0) {
            child = Optional.of(new Node<>(element));
            len += 1;
            return;
        }
        // Due to len constraints, we're guaranteed to have a valid child
        Optional<Node<T>> child = this.child;
        while (child.get().nextNode().isPresent()) {
            // Traverse the node list until we reach the last node
            child = child.get().nextNode();
        }
        len += 1;
        child.get().insert_or_replace_next(element);
    }

    public void insert_first(T element) {
        var tmp = this.child;
        this.child = Optional.of(new Node<>(element));
        this.child.get().next = tmp;
        len += 1;
    }

    public Optional<T> remove_first() {
        if (this.isEmpty()) {
            return Optional.empty();
        }
        // `len` guarantees us to not have an empty child
        var child = this.child.get();
        this.len -= 1;

        // Shift the next node to this place
        this.child = child.nextNode();

        return Optional.of(child.value);
    }

    public Optional<T> remove_last() {
        if (this.isEmpty()) {
            return Optional.empty();
        } else if (len == 1) {
            len = 0;
            // Drops the first index
            var child = this.child.get().value;
            this.child = Optional.empty();
            return Optional.of(child);
        }
        // len check guarantees us that this child is not empty
        Optional<Node<T>> child = this.child;

        for (int i = 0; i < len - 2; i++) {
            // @formatter:off
            // len - 1 => 2;
            //           0      1      2
            // child: [(1,T), (2,T), (3,T), (4,X)]
            // @formatter:on
            // Traverse the node until len-2'th node, and drop the child
            child = child.get().nextNode();
        }
        len -= 1;
        Optional<T> element = child.get().drop();
        return element;
    }

    public Optional<T> get(int idx) {
        if (idx < 0 || idx >= len) {
            return Optional.empty();
        }
        Optional<Node<T>> child = this.child;
        for (int i = 0; i < idx; i++) {
            child = child.get().nextNode();
        }
        return Optional.of(child.get().value);
    }

    public Optional<T> peek_first() {
        return get(0);
    }

    public int size() {
        return len;
    }

    boolean isEmpty() {
        return (len == 0);
    }

    @Override
    public String toString() {
        if (len == 0) {
            return "[]";
        }
        return child.get().toString();
    }

    public int len() {
        return this.len;
    }

    public void clear() {
        this.child = Optional.empty();
        this.len = 0;
    }

    public static void main(String[] args) {
        var list = new LinkedList<Integer>();
        list.insert_last(1);
        System.out.println(list);
        list.insert_last(2);
        System.out.println(list);
        list.insert_last(3);
        System.out.println(list);
        list.insert_last(4);
        System.out.println(list);
        list.remove_last();
        System.out.println(list);
        list.insert_last(5);
        System.out.println(list);
        list.remove_last();
        list.remove_last();
        list.insert_first(0);
        System.out.println(list);
        list.remove_first();
        list.remove_first();
        list.remove_first();
        list.insert_first(20);
        list.insert_first(40);
        System.out.println(list + " " + list.size());
    }

}

class Node<T> {
    T value;
    Optional<Node<T>> next = Optional.empty();

    Node(T value) {
        this.value = value;
    }

    public Optional<Node<T>> nextNode() {
        return next;
    }

    public T getValue() {
        return value;
    }

    public void insert_or_replace_next(T value) {
        next = Optional.of(new Node<>(value));
    }

    // Retrieves the last node that points to null
    public Node<T> traverse() {
        if (next.isPresent()) {
            return next.get().traverse();
        }
        return this;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(value);
        Optional<Node<T>> next = this.nextNode();
        while (next.isPresent()) {
            sb.append(", ");
            sb.append(next.get().value);
            next = next.get().nextNode();
        }

        sb.append("]");
        return sb.toString();
    }

    public Optional<T> drop() {
        if (!next.isPresent())
            return Optional.empty();
        var ret = Optional.of(next.get().value);
        next = Optional.empty();
        return ret;
    }

}

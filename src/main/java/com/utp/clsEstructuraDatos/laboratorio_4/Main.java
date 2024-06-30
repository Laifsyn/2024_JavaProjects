package com.utp.clsEstructuraDatos.laboratorio_4;

import java.util.Optional;

import com.utp.clsEstructuraDatos.Estructuras.linked_list.LEColaCircular;
import com.utp.clsEstructuraDatos.Estructuras.linked_list.LEColaLineal;
import com.utp.clsEstructuraDatos.Estructuras.linked_list.LEPila;
import com.utp.clsEstructuraDatos.Estructuras.linked_list.LinkedList;

public class Main {

}

class App {
    static final String LABORATORIO = "Laboratorio 4";
    Optional<AppCollection<Integer>> collection;

    void new_collection() {}

}

sealed interface AppCollection<T> {
    // @formatter:off
    void insert(T element);
    Optional<T> pop();
    Optional<T> peek();
    boolean isEmpty();
    boolean isFull();
    void clear();
    int len();
    default AppCollection<T> extend_from(AppCollection<T> source){
        switch (source) {
            case Stack<T>(var stack) -> {
                var element = stack.extricate
                this.insert()
            }
            case Queue<T>(var queue) -> {
                for (int i = 0; i < list.len(); i++) {
                    queue.insert(list.get(i).get());
                }
                return new Queue<>(queue);
            }
            case CircularQueue<T>(var circular_queue) -> {
                for (int i = 0; i < list.len(); i++) {
                    circular_queue.insert(list.get(i).get());
                }
                return new CircularQueue<>(circular_queue);
            }
        }
    }
    // @formatter:on

    public record Stack<T>(LEPila<T> pila) implements AppCollection<T> {

        @Override
        public void insert(T element) {
            pila.push(element);
        }

        @Override
        public Optional<T> pop() {
            return Optional.ofNullable(pila.pop());
        }

        @Override
        public Optional<T> peek() {
            return Optional.ofNullable(pila.peek_top());
        }

        @Override
        public boolean isEmpty() {
            return pila.len() == 0;
        }

        @Override
        public boolean isFull() {
            // Since LEPila is backed by a LinkedList, it's never full.
            return false;
        }

        @Override
        public void clear() {
            pila.clear();
        }

        @Override
        public int len() {
            return pila.len();
        }
    }

    public record Queue<T>(LEColaLineal<T> lineal_queue) implements AppCollection<T> {

        @Override
        public void insert(T element) {
            lineal_queue.insert(element);
        }

        @Override
        public Optional<T> pop() {
            return Optional.ofNullable(lineal_queue.remove());
        }

        @Override
        public Optional<T> peek() {
            return Optional.ofNullable(lineal_queue.peek());
        }

        @Override
        public boolean isEmpty() {
            return lineal_queue.len() == 0;
        }

        @Override
        public boolean isFull() {
            // Since LEColaLineal is backed by a LinkedList, it's never full.
            return false;
        }

        @Override
        public void clear() {
            lineal_queue.clear();
        }

        @Override
        public int len() {
            return lineal_queue.len();
        }
    }

    public record CircularQueue<T>(LEColaCircular<T> looped_queue) implements AppCollection<T> {

        @Override
        public void insert(T element) {
            looped_queue.insert(element);
        }

        @Override
        public Optional<T> pop() {
            return Optional.ofNullable(looped_queue.remove());
        }

        @Override
        public Optional<T> peek() {
            return Optional.ofNullable(looped_queue.peek());
        }

        @Override
        public boolean isEmpty() {
            return looped_queue.len() == 0;
        }

        @Override
        public boolean isFull() {
            // Since LEColaCircular is backed by a LinkedList, it's never full.
            return false;
        }

        @Override
        public void clear() {
            looped_queue.clear();
        }

        @Override
        public int len() {
            return looped_queue.len();
        }
    }
}

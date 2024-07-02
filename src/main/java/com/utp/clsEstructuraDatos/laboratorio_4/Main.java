package com.utp.clsEstructuraDatos.laboratorio_4;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Optional;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.utp.clsEstructuraDatos.Estructuras.linked_list.LEColaCircular;
import com.utp.clsEstructuraDatos.Estructuras.linked_list.LEColaLineal;
import com.utp.clsEstructuraDatos.Estructuras.linked_list.LEPila;
import com.utp.clsEstructuraDatos.Estructuras.linked_list.LinkedList;

import static com.utp.clsEstructuraDatos.laboratorio_4.App.AppEvent.*;

public class Main {

}

class App {
    static final String LABORATORIO = "Laboratorio 4";
    AppCollection<Integer> collection = null;
    JFrame frame = new JFrame(LABORATORIO);
    JLabel collection_type = new JLabel(App.UNDEFINED_COLLECTION);
    JLabel message_label = new JLabel("");
    JButton btn_push = new JButton("Push");
    JButton btn_pop = new JButton("Pop");
    JButton btn_peek = new JButton("Peek");
    JButton btn_clear = new JButton("Clear");
    JButton btn_create = new JButton("Create");
    JButton btn_display = new JButton("Display");

    public static final String UNDEFINED_COLLECTION = "Colección no definida";

    App() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        collection_type.setForeground(java.awt.Color.RED);
        collection_type.setHorizontalAlignment(JLabel.CENTER);
        btn_display.setEnabled(false);
        btn_push.setEnabled(false);
        btn_pop.setEnabled(false);
        btn_peek.setEnabled(false);
        btn_clear.setEnabled(false);
        btn_display.addActionListener(e -> SendEvent(new Display("")));
    }

    void new_collection() {
        String[] options = { "Pila", "Cola Lineal", "Cola Circular" };
        int choice = JOptionPane.showOptionDialog(null, "Seleccione el tipo de colección", LABORATORIO,
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        AppCollection<Integer> new_collection = null;
        switch (choice) {
            case 0 -> {
                new_collection = new AppCollection.Stack<>(new LEPila<>());
            }
            case 1 -> {
                new_collection = new AppCollection.Queue<>(new LEColaLineal<>());
            }
            case 2 -> {
                new_collection = new AppCollection.CircularQueue<>(new LEColaCircular<>());
            }
        }
        if (collection == null) {
            collection = new_collection;
            return;
        }
        String prompt = "Quieres mantener los datos antiguos?";

        int keep_data = JOptionPane.showConfirmDialog(null, prompt, LABORATORIO, JOptionPane.YES_NO_OPTION);
        if (keep_data == JOptionPane.YES_OPTION) {
            boolean new_coll_is_same_type_as_self = collection.getClass().equals(new_collection.getClass());
            // skip extending if the new collection is the same type as the old one
            if (new_coll_is_same_type_as_self) {
                return;
            }
            new_collection.extend_from(this.collection);
            this.collection = new_collection;
        } else {
            // No good reason to explicitly clear the old collection.
            this.collection = new_collection;
        }
    }

    void SendEvent(AppEvent event) {
        switch (event) {
            case Push(int element) -> {
                collection.insert(element);
                SendEvent(new Display("Elemento insertado: " + element));
            }
            case Pop() -> {
                if (collection.isEmpty()) {
                    error_dialogue("No se puede quitar de una colección vacía.");
                    return;
                }
                Optional<Integer> popped = collection.pop();
                String popped_string = popped.get().toString();
                SendEvent(new Display("Elemento quitado: " + popped_string));
            }
            case Peek() -> {
                if (collection.isEmpty()) {
                    error_dialogue("No se puede ver el elemento de una colección vacía.");
                    return;
                }
                var elemento = collection.peek();
                String elemento_string = elemento.get().toString();
                SendEvent(new Display("Viendo Elemento: " + elemento_string));
            }
            case Clear() -> {
                collection.clear();
                SendEvent(new Display("Colección limpiada"));
            }
            case Create() -> {
                new_collection();
                if (collection == null) {
                    collection_type.setText(UNDEFINED_COLLECTION);
                    return;
                } else {
                    String collection_type = collection.getClass().getSimpleName();
                    this.collection_type.setText("Tipo de collección: " + collection_type);
                    SendEvent(new Display(String.format("Colección `%s` creada", collection_type)));
                }
            }
            case ExtendFrom(AppCollection<Integer> source) -> {
                collection.extend_from(source);
                throw new UnsupportedOperationException("Not implemented");
            }
            case Display(String msg) -> {
                btn_display.setText(msg);
            }
        }
    }

    void run() {
        frame.add(button_pane());
        frame.pack();
        frame.setVisible(true);
    }

    JPanel content_pane() {
        JPanel content_pane = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        // gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(4, 1, 4, 1);
        gbc.gridx = 0;
        gbc.gridy = 0;
        content_pane.add(collection_type, gbc);
        gbc.gridy = 1;
        content_pane.add(button_pane(), gbc);
        gbc.gridy = 2;
        content_pane.add(message_label, gbc);
        return content_pane;
    }

    JPanel button_pane() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(2, 1, 2, 1);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(btn_create, gbc);
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(btn_push, gbc);
        gbc.gridx = 1;
        panel.add(btn_pop, gbc);
        gbc.gridy = 2;
        gbc.gridx = 0;
        panel.add(btn_peek, gbc);
        gbc.gridx = 1;
        panel.add(btn_clear, gbc);
        gbc.gridy = 3;
        gbc.gridx = 0;
        panel.add(btn_display, gbc);

        return panel;

    }

    void error_dialogue(String msg) {
        JOptionPane.showMessageDialog(null, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public sealed static interface AppEvent {
        // @formatter:off
        public static record Push(int element) implements AppEvent {}
        public static record Pop() implements AppEvent {}
        public static record Peek() implements AppEvent {}
        public static record Clear() implements AppEvent {}
        public static record Create() implements AppEvent {}
        public static record Display(String msg) implements AppEvent {}
        public static record ExtendFrom(AppCollection<Integer> source) implements AppEvent {}
        // @formatter:on
    }
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
    // @formatter:on

    default AppCollection<T> extend_from(AppCollection<T> source) {
        switch (source) {
            case Stack<T>(var stack) -> {
                LinkedList<T> list = stack.to_inverted();
                while (!list.isEmpty()) {
                    this.insert(list.remove_first().get());
                }
            }
            case Queue<T>(LEColaLineal<T> queue) -> {
                while (!queue.isEmpty()) {
                    this.insert(queue.remove());
                }
            }
            case CircularQueue<T>(LEColaCircular<T> circular_queue) -> {
                while (!circular_queue.isEmpty()) {
                    this.insert(circular_queue.remove());
                }
            }
        }
        return this;
    }

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

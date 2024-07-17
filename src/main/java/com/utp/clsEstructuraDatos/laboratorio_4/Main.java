package com.utp.clsEstructuraDatos.laboratorio_4;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Optional;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import com.utp.clsEstructuraDatos.Estructuras.linked_list.LEColaCircular;
import com.utp.clsEstructuraDatos.Estructuras.linked_list.LEColaLineal;
import com.utp.clsEstructuraDatos.Estructuras.linked_list.LEPila;
import com.utp.clsEstructuraDatos.Estructuras.linked_list.LinkedList;

import static com.utp.clsEstructuraDatos.laboratorio_4.App.AppEvent.*;

public class Main {
    public static void main(String[] args) {
        App app = new App();
        app.run();
    }
}

class App {
    static final String LABORATORIO = "Laboratorio 4";
    AppCollection<Integer> collection = null;
    JFrame frame = new JFrame(LABORATORIO);
    JLabel collection_type = new JLabel(App.UNDEFINED_COLLECTION);
    JTextArea message_label = new JTextArea("");
    JButton btn_push = new JButton("Push");
    JButton btn_pop = new JButton("Pop");
    JButton btn_peek = new JButton("Peek");
    JButton btn_clear = new JButton("Clear");
    JButton btn_create = new JButton("Create");
    JButton btn_display = new JButton("Display");

    public static final Color Red = Color.RED;
    public static final Color Green = new Color(1, 113, 72);
    public static final Color Black = Color.BLACK;
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
        btn_display.addActionListener(e -> SendEvent(new Display()));
        btn_pop.addActionListener(e -> SendEvent(new Pop()));
        btn_peek.addActionListener(e -> SendEvent(new Peek()));
        btn_clear.addActionListener(e -> SendEvent(new Clear()));
        btn_create.addActionListener(e -> SendEvent(new Create()));
        message_label.setEditable(false);
        btn_push.addActionListener(e -> {
            String input = JOptionPane.showInputDialog("Ingrese un número entero");
            if (input == null) {
                return;
            }
            try {
                int element = Integer.parseInt(input);
                SendEvent(new Push(element));
            } catch (NumberFormatException ex) {
                error_dialogue("Por favor ingrese un número entero.");
            }
        });
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
                SendEvent(new UpdateMsg("Elemento insertado: " + element, Green));
            }
            case Pop() -> {
                if (collection.isEmpty()) {
                    SendEvent(new UpdateMsg("Collecion vacía", Red));
                    error_dialogue("No se puede quitar de una colección vacía.");
                    return;
                }
                Optional<Integer> popped = collection.pop();
                String popped_string = popped.get().toString();
                SendEvent(new UpdateMsg("Elemento quitado: " + popped_string, Black));
            }
            case Peek() -> {
                if (collection.isEmpty()) {
                    SendEvent(new UpdateMsg("Colección vacía", Red));
                    error_dialogue("No se puede ver el elemento de una colección vacía.");
                    return;
                }
                var elemento = collection.peek();
                String elemento_string = elemento.get().toString();
                SendEvent(new UpdateMsg("Viendo Elemento: " + elemento_string, Black));
            }
            case Clear() -> {
                if (collection.isEmpty()) {
                    SendEvent(new UpdateMsg("La colección ya está vacía", Black));
                    return;
                }
                collection.clear();
                SendEvent(new UpdateMsg("Colección limpiada", Black));
            }
            case Create() -> {
                new_collection();
                if (collection == null) {
                    collection_type.setText(UNDEFINED_COLLECTION);
                    return;
                } else {
                    String collection_type = collection.getClass().getSimpleName();
                    this.collection_type.setText("Tipo de collección: " + collection_type);
                    btn_display.setEnabled(true);
                    btn_push.setEnabled(true);
                    btn_pop.setEnabled(true);
                    btn_peek.setEnabled(true);
                    btn_clear.setEnabled(true);
                    SendEvent(new UpdateMsg(String.format("Colección `%s` creada", collection_type), Green));
                }
            }
            case ExtendFrom(AppCollection<Integer> source) -> {
                collection.extend_from(source);
                throw new UnsupportedOperationException("Not implemented");
            }
            case Display() -> {
                if (collection.isEmpty()) {
                    SendEvent(new UpdateMsg("Colección vacía", Red));
                    return;
                }
                StringBuilder sb = new StringBuilder();
                sb.append("Elementos en la colección: ");
                sb.append(collection.len());
                sb.append("\n");
                sb.append("Elementos: ");
                sb.append(collection);
                SendEvent(new UpdateMsg(sb.toString(), Black));
            }
            case UpdateMsg(String msg, Color clr) -> {
                message_label.setText(msg);
                message_label.setForeground(clr);
                try_pack();
            }
        }
    }

    void run() {
        frame.add(content_pane());
        frame.pack();
        frame.setLocationRelativeTo(null);
        var prefered = frame.getPreferredSize();
        prefered.width = 280;
        frame.setSize(prefered);
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
        gbc.gridwidth = 2;
        panel.add(btn_display, gbc);

        return panel;

    }

    void error_dialogue(String msg) {
        JOptionPane.showMessageDialog(null, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    // Packs the frame to the preferred size, updating if undersized
    public void try_pack() {
        Dimension current_size = frame.getSize();
        Dimension preferred_size = frame.getPreferredSize();
        int new_width = Math.max(current_size.width, preferred_size.width);
        int new_height = Math.max(current_size.height, preferred_size.height);
        if (new_width != current_size.width || new_height != current_size.height)
            frame.setSize(new_width, new_height);
    }

    public sealed static interface AppEvent {
        // @formatter:off
        public static record Push(int element) implements AppEvent {}
        public static record Pop() implements AppEvent {}
        public static record Peek() implements AppEvent {}
        public static record Clear() implements AppEvent {}
        public static record Create() implements AppEvent {}
        public static record Display() implements AppEvent {}
        public static record UpdateMsg(String msg, Color color) implements AppEvent {}
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

    public default AppCollection<T> extend_from(AppCollection<T> source) {
        switch (source) {
            case Stack<T>(var stack) -> {

                if (this instanceof Stack<T>) {
                    LinkedList<T> source_list = stack.to_inverted();
                    while (!source_list.isEmpty()) {
                        this.insert(source_list.remove_first().get());
                    }
                } else {
                    while (!stack.isEmpty()) {
                        this.insert(stack.pop());
                    }
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

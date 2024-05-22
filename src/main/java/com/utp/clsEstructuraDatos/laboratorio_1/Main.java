package com.utp.clsEstructuraDatos.laboratorio_1;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.TextArea;
import java.util.ArrayList;

import com.utp.clsEstructuraDatos.Estructuras.Pila;

public class Main {
    public static void main(String[] args) {
        App app = new App();
        app.start();
    }
}

class App {
    ArrayList<JButton> botones = new ArrayList<>();
    final String laboratorio_1 = "Laboratorio 1";
    Pila<Integer> pila;

    TextArea textArea = new TextArea();

    void start() {
        JFrame frame = new JFrame(laboratorio_1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new GridBagLayout());
        add_button_commands(frame);
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = 2;
        c.gridwidth = 8;
        this.textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        this.textArea.setEditable(false);
        frame.add(this.textArea, c);
        frame.setVisible(true);
    }

    JButton create_stack = new JButton("Crear Pila");
    JButton push = new JButton("Insertar");
    JButton pop = new JButton("Quitar");
    JButton is_empty = new JButton("Pila vacia?");
    JButton is_full = new JButton("Pila llena?");
    JButton clear = new JButton("Limpiar Pila");
    JButton peek = new JButton("Ver Cima");
    JButton display = new JButton("Imprimir Pila");

    void add_button_commands(JFrame frame) {
        // Deshabilitar botones que solo pueden ser llamados con una pila instanciada.
        this.push.setEnabled(false);
        this.pop.setEnabled(false);
        this.is_empty.setEnabled(false);
        this.is_full.setEnabled(false);
        this.clear.setEnabled(false);
        this.peek.setEnabled(false);
        this.display.setEnabled(false);

        this.create_stack.addActionListener(e -> {
            String input = JOptionPane.showInputDialog("Ingrese el tamaño de la pila");
            if (input == null || input.isEmpty()) {
                error_dialogue("Entrada vacía");
                return;
            }
            try {
                int value = Integer.parseInt(input);
                event_manager(new Command.CreateStack(value));
            } catch (Exception ex) {
                error_dialogue(String.format("`%s` no puede ser convertido a entero", input));
            }
        });
        this.push.addActionListener(e -> {
            String input = JOptionPane.showInputDialog("Ingrese el valor a insertar");
            if (input == null || input.isEmpty()) {
                error_dialogue("Entrada vacía");
                return;
            }
            try {
                int value = Integer.parseInt(input);
                event_manager(new Command.Push(value));
            } catch (Exception ex) {
                error_dialogue(String.format("`%s` no puede ser convertido a entero", input));
            }
        });
        this.pop.addActionListener(e -> event_manager(new Command.Pop()));
        this.is_empty.addActionListener(e -> event_manager(new Command.isEmpty()));
        this.is_full.addActionListener(e -> event_manager(new Command.isFull()));
        this.clear.addActionListener(e -> event_manager(new Command.Clear()));
        this.peek.addActionListener(e -> event_manager(new Command.Peek()));
        this.display.addActionListener(e -> event_manager(new Command.DisplayStack()));

        frame.add(this.create_stack);
        frame.add(this.push);
        frame.add(this.pop);
        frame.add(this.is_empty);
        frame.add(this.is_full);
        frame.add(this.clear);
        frame.add(this.peek);
        frame.add(this.display);

        return;
    }

    void event_manager(Command cmd) {
        switch (cmd) {
            case Command.CreateStack cs -> {
                if (this.pila != null) {
                    // Confirmar deshacer la pila actual
                    int option = JOptionPane.showConfirmDialog(null, "Desea reemplazar la pila actual?", laboratorio_1,
                            JOptionPane.YES_NO_OPTION);
                    if (option == JOptionPane.NO_OPTION)
                        return;
                }
                this.pila = new Pila<Integer>(cs.value());

                this.create_stack.setText("Reemplazar Pila");
                // habilitar botones que solo pueden ser llamados con una pila instanciada.
                this.push.setEnabled(true);
                this.pop.setEnabled(true);
                this.is_empty.setEnabled(true);
                this.is_full.setEnabled(true);
                this.clear.setEnabled(true);
                this.peek.setEnabled(true);
                this.display.setEnabled(true);

                JOptionPane.showMessageDialog(null,
                        String.format("Se creo una pila de capacidad para %d enteros", cs.value()),
                        laboratorio_1, JOptionPane.INFORMATION_MESSAGE);
            }
            case Command.Push(Integer value) -> {
                if (this.pila.insertar(value))
                    JOptionPane.showMessageDialog(null, String.format("Se inserto el valor %d", value), laboratorio_1,
                            JOptionPane.INFORMATION_MESSAGE);
                else
                    error_dialogue("La pila esta llena");
            }
            case Command.Pop() -> {
                if (this.pila.pilaVacia()) {
                    error_dialogue("La pila esta vacia");
                    return;
                }
                JOptionPane.showMessageDialog(null, String.format("Se elimino el valor %d", this.pila.quitarCima()),
                        laboratorio_1, JOptionPane.INFORMATION_MESSAGE);
            }
            case Command.isEmpty() -> {
                final String msg = (this.pila.pilaVacia() ? "La pila esta vacia" : "La pila no esta vacia")
                        + String.format(". Tiene %d elementos", this.pila.getCima());
                JOptionPane.showMessageDialog(null, msg, laboratorio_1, JOptionPane.INFORMATION_MESSAGE);
            }
            case Command.isFull() -> {
                final String msg = (this.pila.pilaLLena() ? "La pila esta llena" : "La pila no esta llena")
                        + String.format(". Le quedan %d espacios", this.pila.getCapacidad() - this.pila.getCima());
                JOptionPane.showMessageDialog(null, msg, laboratorio_1, JOptionPane.INFORMATION_MESSAGE);
            }
            case Command.Clear() -> {
                this.pila.limpiarPila();
                JOptionPane.showMessageDialog(null, "Se limpio la pila", laboratorio_1,
                        JOptionPane.INFORMATION_MESSAGE);
            }
            case Command.Peek() -> {
                if (this.pila.pilaVacia()) {
                    error_dialogue("La pila esta vacia");
                    return;
                }
                JOptionPane.showMessageDialog(null, String.format("El valor en la cima es %d", this.pila.verCima()),
                        laboratorio_1, JOptionPane.INFORMATION_MESSAGE);
            }
            case Command.DisplayStack() -> {
                TextArea textArea = new TextArea(this.pila.imprimirPila());
                textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
                textArea.setEditable(false);
                JOptionPane.showMessageDialog(null, textArea, laboratorio_1,
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }
        this.textArea.setText(this.pila.imprimirPila());
    }

    void error_dialogue(String msg) {
        JOptionPane.showMessageDialog(null, msg, laboratorio_1, JOptionPane.ERROR_MESSAGE);
    }
}

// @formatter:off
sealed interface Command {
    public record CreateStack(int value) implements Command {}
    public record Push(Integer value) implements Command {}
    public record Pop() implements Command {}
    public record isEmpty() implements Command {}
    public record isFull() implements Command {}
    public record Clear() implements Command {}
    public record Peek() implements Command {}
    public record DisplayStack() implements Command{}
}
// @formatter:on
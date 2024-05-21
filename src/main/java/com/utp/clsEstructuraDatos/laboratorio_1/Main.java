package com.utp.clsEstructuraDatos.laboratorio_1;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.awt.GridBagLayout;
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

    void start() {
        JFrame frame = new JFrame(laboratorio_1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new GridBagLayout());
        add_button_commands(frame);
        frame.setVisible(true);
    }

    void add_button_commands(JFrame frame) {
        JButton create_stack = new JButton("Crear Pila");
        JButton push = new JButton("Insertar");
        JButton pop = new JButton("Quitar");
        JButton is_empty = new JButton("Pila vacia?");
        JButton is_full = new JButton("Pila llena?");
        JButton clear = new JButton("Limpiar Pila");
        JButton peek = new JButton("Ver Cima");
        JButton display = new JButton("Imprimir Pila");
        // Insertar los botones creados al arreglo
        // this.botones.add(create_stack);
        // this.botones.add(push);
        // this.botones.add(pop);
        // this.botones.add(is_empty);
        // this.botones.add(is_full);
        // this.botones.add(clear);
        // this.botones.add(peek);
        // this.botones.add(display);

        
        create_stack.addActionListener(e -> {
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
        frame.add(create_stack);

        return;
    }

    void event_manager(Command cmd) {
        switch (cmd) {
            case Command.CreateStack cs -> {
                this.pila = new Pila<Integer>(cs.value());
                JOptionPane.showMessageDialog(null, String.format("Creo una pila de %d enteros", cs.value()),
                        laboratorio_1, JOptionPane.INFORMATION_MESSAGE);
            }
            default -> {
                throw new RuntimeException("Comando no soportado " + cmd.toString());
            }
        }
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
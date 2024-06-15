package com.utp.clsEstructuraDatos.laboratorio_3;

import javax.management.RuntimeErrorException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.utp.clsEstructuraDatos.Estructuras.colas.AbstractCola;
import com.utp.clsEstructuraDatos.Estructuras.colas.ColaCircular;
import com.utp.clsEstructuraDatos.Estructuras.colas.ColaSimple;

import static com.utp.clsEstructuraDatos.laboratorio_3.ColasApp.CMD.*;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

public class Main {
    public static void main(String[] args) {
        ColasApp app = new ColasApp();
        app.run_app();
    }
}

class ColasApp {
    static public String LABORATORIO = "Laboratorio 3";
    final JButton btn_crear_cola = new JButton("Crear Cola");
    final JButton btn_insertar = new JButton("Insertar");
    final JButton btn_quitar = new JButton("Quitar");
    final JButton btn_limpiar = new JButton("Limpiar");
    final JButton btn_mostrar = new JButton("Mostrar");
    final JFrame frame = new JFrame(LABORATORIO);
    AbstractCola<Integer> cola;

    static int COLA_CIRCULAR = 0;
    static int COLA_SIMPLE = 1;

    ColasApp() {}

    public void run_app() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(null);
        frame.add(content());
        this.send_command(null);
    }

    JPanel content() {
        JPanel content = new JPanel();
        content.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints(0, 0, 2, 3, 0, 0, GridBagConstraints.CENTER, 0,
                new Insets(5, 5, 5, 5), 0, 0);

        c.gridwidth = 2;
        content.add(this.btn_crear_cola, c);

        c.gridy = 1;
        c.gridwidth = 1;
        content.add(this.btn_insertar, c);
        c.gridx = 1;
        content.add(this.btn_quitar, c);

        c.gridy = 2;
        c.gridx = 0;
        content.add(this.btn_limpiar, c);
        c.gridx = 1;
        content.add(this.btn_mostrar, c);

        c.gridy = 3;
        c.gridx = 0;

        
        return content;
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

    public int prompt_colas_type() {
        int num = JOptionPane.showOptionDialog(frame, "Seleccione el tipo de cola a utilizar",
                LABORATORIO + ": Tipo de Cola",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                new String[] { "Cola Circular", "Cola Simple" }, "Cola Circular");
        System.out.println("Seleccion del tipo: " + num);
        return num;
    }

    void switch_buttons(boolean value) {
        this.btn_insertar.setEnabled(value);
        this.btn_quitar.setEnabled(value);
        this.btn_limpiar.setEnabled(value);
        this.btn_mostrar.setEnabled(value);
    }

    // Event Manager
    public void send_command(CMD command) {
        switch (command) {
            case Pack() -> {
                this.try_pack();
            }
            case CrearCola(int capacidad_cola) -> {
                int cola = prompt_colas_type();
                if (cola == COLA_CIRCULAR) {
                    this.cola = new ColaCircular<>(capacidad_cola);
                } else {
                    this.cola = new ColaSimple<>(capacidad_cola);
                }
                String new_text = String.format("[%s (c:%d)]Recrear Cola", this.cola.getClass().getSimpleName(),
                        this.cola.capacity());
                this.btn_crear_cola.setText(new_text);
                switch_buttons(true);
            }
            case Insertar<Integer>(Integer elemento) -> {
                this.cola.insertar(elemento);
            }
            case Quitar() -> {
                this.cola.quitar();
            }
            case Limpiar() -> {
                this.cola.limpiar();
            }
            case Mostrar() -> {
                throw new RuntimeErrorException(null, "Not Implemented");
            }
            case null -> {
            }
            default -> {
                throw new RuntimeErrorException(null, "Unsopported Command " + command.getClass().getName());
            }
        }
        if (this.cola == null) {
            this.btn_crear_cola.setText("Crear Cola");
            switch_buttons(false);
        }
    }

    public static sealed interface CMD {
        // @formatter:off
        public static record Pack() implements CMD {}
        public static record CrearCola(int capacidad_cola) implements CMD {}
        public static record Insertar<T>(T elemento) implements CMD {}
        public static record Quitar() implements CMD {}
        public static record Limpiar() implements CMD {}
        public static record Mostrar() implements CMD {}
        // @formatter:on
    }
}

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
    final JPanel display_area = new JPanel();
    final JFrame frame = new JFrame(LABORATORIO);
    AbstractCola<Integer>.PanelDrawer drawer;
    AbstractCola<Integer> cola;

    static int COLA_CIRCULAR = 0;
    static int COLA_SIMPLE = 1;

    public void run_app() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.add(content());
        frame.setVisible(true);
        frame.pack();
        var pref_size = frame.getPreferredSize();
        int pref_width = Math.max(pref_size.width, 275);
        frame.setSize(pref_width, pref_size.height);
        this.send_command(null);
    }

    ColasApp() {
        this.btn_crear_cola.addActionListener(e -> {

            String input = JOptionPane.showInputDialog(frame, "Ingrese la capacidad de la cola", "Crear Cola",
                    JOptionPane.QUESTION_MESSAGE);
            try {
                Integer.parseInt(input);
            } catch (NumberFormatException e1) {
                JOptionPane.showMessageDialog(frame, "Debe ingresar un número entero",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            int capacidad_cola = Integer.parseInt(input);
            this.send_command(new CrearCola(capacidad_cola));
        });
        this.btn_insertar.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(frame, "Ingrese el elemento a insertar", "Insertar",
                    JOptionPane.QUESTION_MESSAGE);
            try {
                Integer.parseInt(input);
            } catch (NumberFormatException e1) {
                JOptionPane.showMessageDialog(frame, "Debe ingresar un número entero", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            int elemento = Integer.parseInt(input);
            this.send_command(new Insertar<>(elemento));
        });
        this.btn_quitar.addActionListener(e -> {
            this.send_command(new Quitar());
        });
        this.btn_limpiar.addActionListener(e -> {
            this.send_command(new Limpiar());
        });
        this.btn_mostrar.addActionListener(e -> {
            this.send_command(new Mostrar());
        });
    }

    JPanel content() {
        JPanel content = new JPanel();
        content.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.insets.set(2, 3, 2, 3);
        c.gridx = 0;
        c.gridy = 0;
        content.add(buttons_panel(), c);

        c.gridy = 1;
        content.add(display_area, c);
        return content;
    }

    JPanel buttons_panel() {
        JPanel buttons_panel = new JPanel();
        buttons_panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.insets.set(2, 3, 2, 3);

        c.gridwidth = 2;
        buttons_panel.add(this.btn_crear_cola, c);

        c.gridy = 1;
        c.gridwidth = 1;
        buttons_panel.add(this.btn_insertar, c);
        c.gridx = 1;
        buttons_panel.add(this.btn_quitar, c);

        c.gridy = 2;
        c.gridx = 0;
        buttons_panel.add(this.btn_limpiar, c);
        c.gridx = 1;
        buttons_panel.add(this.btn_mostrar, c);

        c.gridy = 3;
        c.gridx = 0;

        return buttons_panel;
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
            case Redraw() -> {
                if (this.cola == null)
                    return;
                else if (this.drawer.cola() < this.drawer.frente() || (this.drawer.longitud() == this.drawer.capacity()
                        && this.drawer.cola() <= this.drawer.frente()))
                    this.drawer.wrapped();
                else
                    this.drawer.aligned();
                this.try_pack();
            }
            case CrearCola(int capacidad_cola) -> {
                int cola = prompt_colas_type();
                if (this.cola != null) {
                    this.cola.limpiar();
                    this.drawer.aligned();
                }
                if (cola == COLA_CIRCULAR) {
                    this.cola = new ColaCircular<>(capacidad_cola);
                } else {
                    this.cola = new ColaSimple<>(capacidad_cola);
                }
                String new_text = String.format("[%s (c:%d)]Recrear Cola", this.cola.getClass().getSimpleName(),
                        this.cola.capacity());
                this.btn_crear_cola.setText(new_text);
                switch_buttons(true);
                this.drawer = this.cola.as_drawer();
                this.display_area.removeAll();
                this.display_area.add(drawer.as_panel());
                drawer.aligned();
                this.send_command(new Redraw());
                this.try_pack();
            }
            case Insertar<Integer>(Integer elemento) -> {
                this.cola.insertar(elemento);
                this.send_command(new Redraw());
            }
            case Quitar() -> {
                this.cola.quitar();
                this.send_command(new Redraw());
            }
            case Limpiar() -> {
                this.cola.limpiar();
                this.send_command(new Redraw());
            }
            case Mostrar() -> {
                JOptionPane.showMessageDialog(frame, this.cola.toString(), "Cola", JOptionPane.INFORMATION_MESSAGE);
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
        } else { // Only operations that can be done on a non-null queue
            if (this.cola.len() <= 0) {
                this.btn_quitar.setEnabled(false);
                this.btn_quitar.setToolTipText("No hay elementos para quitar");
                this.btn_limpiar.setEnabled(false);
                this.btn_limpiar.setToolTipText("No hay elementos para limpiar");
            } else {
                this.btn_quitar.setEnabled(true);
                this.btn_quitar.setToolTipText(null);
                this.btn_limpiar.setEnabled(true);
                this.btn_limpiar.setToolTipText(null);
            }

            if (this.cola.len() == this.cola.capacity()) {
                this.btn_insertar.setEnabled(false);
                this.btn_insertar.setToolTipText("La cola está llena");
            } else {
                this.btn_insertar.setEnabled(true);
                this.btn_insertar.setToolTipText(null);
            }
        }
    }

    public static sealed interface CMD {
        // @formatter:off
        /**
         * Agranda la ventana al tamaño preferido, actualizando si está subdimensionada.
         */
        public static record Pack() implements CMD {}
        public static record CrearCola(int capacidad_cola) implements CMD {}
        public static record Insertar<T>(T elemento) implements CMD {}
        public static record Quitar() implements CMD {}
        public static record Limpiar() implements CMD {}
        public static record Mostrar() implements CMD {}
        public static record Redraw() implements CMD {}
        // @formatter:on
    }
}

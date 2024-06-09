
package com.utp.clsEstructuraDiscretas.pry4;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.utp.clsEstructuraDiscretas.pry4.componentes.Entrada_JTextField;
import com.utp.clsEstructuraDiscretas.pry4.componentes.Preguntas;
import com.utp.clsEstructuraDiscretas.pry4.componentes.SiNoRadioButton;
import com.utp.utils.Result;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.math.BigInteger;
import java.util.ArrayList;

/*
 * Conclusiones: Idealmente se quiere siempre insertar JPanels al JFrame. Esto tiende a permitir mucha mayor 
 * flexibilidad y mantenibilidad en el codigo, como lo es detallado por `Hovercraft Full Of Eels` en 
 * {https://stackoverflow.com/questions/13907202/adding-components-into-jpanel-inside-a-jframe} en 2012
 */
/*
 * Fuentes Bibliográficas
 * [Como cambiar el color JLabel](https://stackoverflow.com/questions/2966334/how-do-i-set-the-colour-of-a-label-coloured-text-in-java)
 * [Como acomodar el titulo de TitledBorder](https://stackoverflow.com/questions/36405080/why-is-my-titled-border-panel-so-small)
 * [Paneles dentro de Paneles](https://stackoverflow.com/questions/13292964/container-inside-a-container-in-java)
 * [Como usar Radio Button](https://www.javatpoint.com/java-jradiobutton)
 */
public class Main {
    public static void main(String[] args) {
        (new App()).run_app();
    }
}

class App {
    String pry4 = "Proyecto 4 - Combinatorias";
    JFrame frame = new JFrame(pry4);
    final Entrada_JTextField entradas;
    final Preguntas preguntas;
    final JLabel resultado;

    public App() {
        this.entradas = new Entrada_JTextField(10);
        this.preguntas = new Preguntas(new SiNoRadioButton(), new SiNoRadioButton(), new SiNoRadioButton());
        this.resultado = new JLabel();
        this.resultado.setFont(new Font(Font.MONOSPACED, Font.BOLD, 14));
        // Acopla evento para calcular el resultado
        entradas.addActionListener(e -> {
            SendCommand(new Commands.CalculateCombinatoria(entradas.n.getText(), entradas.r.getText()));
            try_pack();
        });
        preguntas.addActionListener(e -> {
            SendCommand(new Commands.CalculateCombinatoria(entradas.n.getText(), entradas.r.getText()));

            if (preguntas.tipo_formula() == VarianteFormula.PERMUTACIONES_REPETIDAS) {
                entradas.r_label.setText("r : a,b,c,...");
                entradas.r_label.setForeground(Color.MAGENTA);
            } else {
                entradas.r_label.setText("r");
                entradas.r_label.setForeground(Color.BLACK);
            }
            try_pack();
        });
    }

    void try_pack() {
        Dimension current_size = frame.getSize();
        Dimension preferred_size = frame.getPreferredSize();
        int new_width = Math.max(current_size.width, preferred_size.width);
        int new_height = Math.max(current_size.height, preferred_size.height);
        if (new_width != current_size.width || new_height != current_size.height)
            frame.setSize(new_width, new_height);
    }

    void run_app() {
        // frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridBagLayout());
        JPanel panel_final = new JPanel();
        panel_final.setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);
        constraints.gridx = 0;
        constraints.gridy = 0;

        // Construimos el panel final
        panel_final.add(entradas.as_panel(), constraints);
        constraints.gridx++;
        panel_final.add(preguntas.as_panel(), constraints);
        constraints.gridx = 0;
        constraints.gridy++;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.EAST;
        panel_final.add(resultado, constraints);

        frame.add(panel_final);
        frame.pack();
        frame.repaint();
        frame.setMinimumSize(frame.getSize());
        frame.setVisible(true);
    }

    // Thousand separated BigInteger
    String pretty_big_int(BigInteger big_int) {
        if (big_int.compareTo(BigInteger.valueOf(1000)) < 0) {
            return String.valueOf(big_int);
        }
        ArrayList<Integer> triplets = new ArrayList<>();
        BigInteger acc = big_int;
        // Insertar Triplets en LowEndian
        while (acc.compareTo(BigInteger.ZERO) > 0) {
            triplets.add(acc.mod(BigInteger.valueOf(1000)).intValue());
            acc = acc.divide(BigInteger.valueOf(1000));
        }
        StringBuilder builder = new StringBuilder();
        builder.append(triplets.remove(triplets.size() - 1));
        for (int i = triplets.size() - 1; i >= 0; i--) {
            builder.append(",");
            builder.append(String.format("%03d", triplets.get(i)));
        }
        String bigint_string = big_int.toString();
        final int MAX_LENGTH = 20 * 3;
        if (bigint_string.length() >= MAX_LENGTH) {
            String substring = builder.toString().substring(0, MAX_LENGTH);
            int commas = substring.length() - substring.replace(",", "").length();
            return substring + "...+" + (commas + bigint_string.length() - MAX_LENGTH);
        }
        return builder.toString();
    }

    public void SendCommand(Commands command) {
        switch (command) {
            case Commands.CalculateCombinatoria(String n, String r) -> {
                Result<BigInteger, Exception> respuesta = preguntas.tipo_formula().calcular(n, r);
                if (respuesta.isError()) {
                    resultado.setText("Error: " + respuesta.unwrapError().getMessage());
                    resultado.setForeground(Color.RED);
                } else {
                    resultado.setText("Resultado: " + pretty_big_int(respuesta.unwrapOk()));
                    resultado.setForeground(new Color(24, 90, 55));
                }
            }
            case Commands.RepaintAll() -> {
                frame.repaint();
            }
        }
    }
}


// @formatter:off
sealed interface Commands {
    public record CalculateCombinatoria(String n, String r) implements Commands {}
    public record RepaintAll() implements Commands {}
}
// @formatter:on

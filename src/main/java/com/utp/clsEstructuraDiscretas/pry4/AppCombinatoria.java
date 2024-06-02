
package com.utp.clsEstructuraDiscretas.pry4;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import com.utp.utils.Result;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.math.BigInteger;

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
public class AppCombinatoria {
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
        this.resultado = new JLabel("Resultado:");

        // Acopla evento para calcular el resultado
        entradas.addActionListener(e -> {
            SendCommand(new Commands.CalculateCombinatoria(entradas.n.getText(), entradas.r.getText()));
        });
        preguntas.addActionListener(e -> {
            SendCommand(new Commands.CalculateCombinatoria(entradas.n.getText(), entradas.r.getText()));
        });
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
        panel_final.add(resultado, constraints);

        frame.add(panel_final);
        frame.pack();
        frame.repaint();
        frame.setMinimumSize(frame.getSize());
        frame.setVisible(true);
    }

    public void SendCommand(Commands command) {
        switch (command) {
            case Commands.CalculateCombinatoria(String n, String r) -> {
                Result<BigInteger, Exception> respuesta = preguntas.tipo_formula().calcular(n, r);
                if (respuesta.isError()) {
                    resultado.setText("Error: " + respuesta.unwrapError().getMessage());
                    resultado.setForeground(Color.RED);
                } else {
                    resultado.setText("Resultado: " + respuesta.unwrapOk());
                    resultado.setForeground(new Color(24, 90, 55));
                }
            }
            case Commands.RepaintAll() -> {
                frame.repaint();
            }
        }
    }
}

class Entrada_JTextField {
    public final JTextField n;
    public final JTextField r;

    public Entrada_JTextField(int columns) {
        this.n = new JTextField();
        this.r = new JTextField();
        this.n.setColumns(columns);
        this.r.setColumns(columns);
    }

    JPanel panel_de(JTextField field, String label) {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(0, 5, 0, 5);
        constraints.gridx = 0;
        panel.add(new JLabel(label), constraints);
        constraints.gridx++;
        panel.add(field, constraints);
        return panel;
    }

    public JPanel as_panel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(0, 5, 0, 5);
        constraints.gridx = 0;
        constraints.gridy = 0;
        panel.add(panel_de(n, "n"), constraints);
        constraints.gridy++;
        panel.add(panel_de(r, "r"), constraints);
        return panel;
    }

    public void addActionListener(ActionListener l) {
        var key_adapter = new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                l.actionPerformed(null);
            }
        };
        n.addKeyListener(key_adapter);
        r.addKeyListener(key_adapter);
    }

}

class Preguntas {
    public final SiNoRadioButton orden;
    public final SiNoRadioButton repetible;
    public final SiNoRadioButton exhaustivo;

    public Preguntas(SiNoRadioButton orden, SiNoRadioButton exhaustivo, SiNoRadioButton repetible) {
        this.orden = orden;
        this.repetible = repetible;
        this.exhaustivo = exhaustivo;

        exhaustivo.setEnabled(orden.is_yes());
        orden.addActionListener(e -> {
            exhaustivo.setEnabled(orden.is_yes());
        });
    }

    public VarianteFormula tipo_formula() {
        if (orden.is_yes()) {
            if (exhaustivo.is_yes()) {
                if (repetible.is_yes()) {
                    return VarianteFormula.VARIANZA_REPETIDAS;
                } else {
                    return VarianteFormula.PERMUTACIONES;
                }
            } else {
                if (repetible.is_yes()) {
                    return VarianteFormula.PERMUTACIONES_REPETIDAS;
                } else {
                    return VarianteFormula.VARIANZA;
                }
            }
        } else {
            if (repetible.is_yes()) {
                return VarianteFormula.COMBINACIONES_REPETIDAS;
            } else {
                return VarianteFormula.COMBINACIONES;
            }
        }
    }

    public JPanel as_panel() {
        JPanel panel = new JPanel(new GridBagLayout());
        // Acoplamos los RadioButtons
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(0, 5, 0, 5);
        constraints.gridx = 0;
        panel.add(orden.as_panel("¿Importa el orden?"), constraints);
        constraints.gridx++;
        panel.add(exhaustivo.as_panel("¿Se usan todos?"), constraints);
        constraints.gridx++;
        panel.add(repetible.as_panel("¿Se pueden repetir los elementos?"), constraints);

        // Acoplamos el Titulo del contenedor
        var titulo = BorderFactory.createTitledBorder("Tipo de Problema: " + this.tipo_formula().toString());
        this.addActionListener(e -> {
            VarianteFormula tipo_de_formula = this.tipo_formula();
            titulo.setTitleColor(tipo_de_formula.as_color());
            titulo.setTitle("Tipo de Problema: " + tipo_de_formula.toString());
            panel.repaint();
        });
        panel.setBorder(titulo);

        return panel;
    }

    public void addActionListener(ActionListener l) {
        orden.addActionListener(l);
        repetible.addActionListener(l);
        exhaustivo.addActionListener(l);

    }
}
class SiNoRadioButton {
    public final JRadioButton si;
    public final JRadioButton no;
    public final ButtonGroup group;

    public SiNoRadioButton() {
        this.si = new JRadioButton("Si");
        this.no = new JRadioButton("No");
        this.group = new ButtonGroup();
        group.add(si);
        group.add(no);
        Rectangle bounds = new Rectangle(20, 20);
        si.setBounds(bounds);
    }

    public JPanel as_panel(String msg) {
        JPanel panel = new JPanel(new GridBagLayout());
        var border = BorderFactory.createTitledBorder(msg);
        border.setTitleFont(new Font(Font.MONOSPACED, Font.BOLD, 14));
        panel.setBorder(border);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(0, 20, 0, 20);
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.gridx = 0;
        constraints.gridy = 0;
        panel.add(si, constraints);
        constraints.gridx++;
        panel.add(no, constraints);

        // Arreglar que el titulo no se corte
        Dimension border_size = border.getMinimumSize(panel);
        Dimension prefered_size = panel.getPreferredSize();
        Insets panel_insets = panel.getInsets();
        int border_width = panel_insets.left + border_size.width + panel_insets.right + 20;
        int prefered_width = Math.max(prefered_size.width, border_width);
        panel.setPreferredSize(new Dimension(prefered_width, prefered_size.height));

        // repintar el panel
        Color dark_green = new Color(1, 113, 72);
        Runnable repaint = () -> {
            if (is_yes()) {
                border.setTitleColor(dark_green);
            } else {
                border.setTitleColor(Color.RED);
            }
            panel.repaint();
        };

        this.no.addActionListener(e -> {
            repaint.run();
        });
        this.si.addActionListener(e -> {
            repaint.run();
        });

        this.no.setSelected(true);
        repaint.run();
        return panel;
    }

    public void addActionListener(ActionListener l) {
        si.addActionListener(l);
        no.addActionListener(l);
    }

    public void setEnabled(boolean enabled) {
        si.setEnabled(enabled);
        no.setEnabled(enabled);
    }

    public boolean is_yes() {
        return si.isSelected();
    }
}

// @formatter:off
sealed interface Commands {
    public record CalculateCombinatoria(String n, String r) implements Commands {}
    public record RepaintAll() implements Commands {}
}
// @formatter:on


enum VarianteFormula {
    PERMUTACIONES, COMBINACIONES, VARIANZA, PERMUTACIONES_REPETIDAS, COMBINACIONES_REPETIDAS, VARIANZA_REPETIDAS;

    static final Color Azul = new Color(1, 75, 160);
    static final Color Morado = new Color(163, 73, 164);
    static final Color Dorado = new Color(239, 184, 16);
    static final Color Chocolate = new Color(105, 63, 38);
    static final Color AzulClaro = new Color(0, 171, 240);
    static final Color Magenta = new Color(200, 0, 167);

    public Color as_color() {
        switch (this) {
            case PERMUTACIONES -> {
                return Azul;
            }
            case COMBINACIONES -> {
                return Morado;
            }
            case VARIANZA -> {
                return Dorado;
            }
            case PERMUTACIONES_REPETIDAS -> {
                return Chocolate;
            }
            case COMBINACIONES_REPETIDAS -> {
                return AzulClaro;
            }
            case VARIANZA_REPETIDAS -> {
                return Magenta;
            }
            default -> {
                return Color.BLACK;
            }
        }

    }

    public Result<BigInteger, Exception> calcular(String n, String r) {
        switch (this) {
            case PERMUTACIONES -> {
                return Formulas.Permutacion_sin_repeticion(new String[] { n, r });
            }
            case COMBINACIONES -> {
                return Formulas.Comb_sin_repeticion(new String[] { n, r });
            }
            case VARIANZA -> {
                return Formulas.Varianza_sin_repeticion(new String[] { n, r });
            }
            case PERMUTACIONES_REPETIDAS -> {
                return Formulas.Permutacion_con_repeticion(new String[] { n, r });
            }
            case COMBINACIONES_REPETIDAS -> {
                return Formulas.Comb_con_repeticion(new String[] { n, r });
            }
            case VARIANZA_REPETIDAS -> {
                return Formulas.Varianza_con_repeticion(new String[] { n, r });
            }
        }
        return Result.error(new UnsupportedOperationException("No se ha implementado la formula"));
    }
}


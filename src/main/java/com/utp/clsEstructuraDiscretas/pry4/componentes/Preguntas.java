package com.utp.clsEstructuraDiscretas.pry4.componentes;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import com.utp.clsEstructuraDiscretas.pry4.VarianteFormula;

public class Preguntas {
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
                    return VarianteFormula.PERMUTACIONES_REPETIDAS;
                } else {
                    return VarianteFormula.PERMUTACIONES;
                }
            } else {
                if (repetible.is_yes()) {
                    return VarianteFormula.VARIANZA_REPETIDAS;
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
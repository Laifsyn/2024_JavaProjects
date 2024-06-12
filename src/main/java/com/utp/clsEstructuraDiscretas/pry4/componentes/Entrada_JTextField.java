package com.utp.clsEstructuraDiscretas.pry4.componentes;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Entrada_JTextField {
    public final JTextField n;
    public final JTextField r;
    final JLabel n_label;
    public final JLabel r_label;

    public Entrada_JTextField(int columns) {
        this.n_label = new JLabel("n");
        this.r_label = new JLabel("r");
        this.n = new JTextField();
        this.r = new JTextField();
        this.n.setColumns(columns);
        this.r.setColumns(columns);
    }

    JPanel panel_de(JTextField field, JLabel label) {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(0, 5, 0, 5);
        constraints.gridx = 0;
        panel.add(label, constraints);
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
        constraints.anchor = GridBagConstraints.EAST;
        panel.add(panel_de(n, n_label), constraints);
        constraints.gridy++;
        panel.add(panel_de(r, r_label), constraints);
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

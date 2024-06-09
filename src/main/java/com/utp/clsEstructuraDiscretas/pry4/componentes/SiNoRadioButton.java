package com.utp.clsEstructuraDiscretas.pry4.componentes;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class SiNoRadioButton {
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

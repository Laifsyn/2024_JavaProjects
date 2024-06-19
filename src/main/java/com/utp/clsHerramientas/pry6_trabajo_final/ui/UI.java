package com.utp.clsHerramientas.pry6_trabajo_final.ui;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;

public final class UI {
    public static final Color Black = Color.BLACK;
    public static final Color DarkCyan = new Color(0, 102, 204);
    public static final Color DarkGreen = new Color(0, 153, 76);
    public static final Color Gray = new Color(192, 192, 192);
    public static final Color Violet = new Color(153, 0, 153);

    public static class ProjFont {
        public static final Font HEADER = new Font(Font.MONOSPACED, Font.BOLD, 14);
        public static final Font NORMAL = new Font(Font.MONOSPACED, Font.PLAIN, 12);
    }

    public static JFrame show(JPanel panel) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        return frame;
    }
}

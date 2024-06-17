package com.utp.clsHerramientas.pry6_trabajo_final.ui;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import com.utp.clsHerramientas.pry6_trabajo_final.Cliente;
import com.utp.clsHerramientas.pry6_trabajo_final.Factura;

public final class Table {
    static final Integer MAX_HEIGHT = 300;
    static final Color DarkCyan = UI.DarkCyan;
    static final Color DarkGreen = UI.DarkGreen;
    final Factura[] facturas;
    final Cliente cliente;

    final JLabel[] encabezados = { new JLabel("FECHA dd/MM/yyyy"), new JLabel("NUM. FACTURA"), new JLabel("MONTO"),
            new JLabel("MES CORRIENTE"),
            new JLabel("30 DÍAS"), new JLabel("60 DÍAS"), new JLabel("90 DÍAS"), new JLabel("+120 DÍAS") };
    final JPanel[] columnas = { new JPanel(), new JPanel(), new JPanel(), new JPanel(), new JPanel(), new JPanel(),
            new JPanel(), new JPanel() };
    final BigDecimal[] montos = { new BigDecimal("0.00"), new BigDecimal("0.00"), new BigDecimal("0.00"),
            new BigDecimal("0.00"), new BigDecimal("0.00"), new BigDecimal("0.00") };
    final static Integer[] rangos = { 0, 30, 60, 90, 120 };

    Table(Factura[] facturas, Cliente cliente) {
        // this.facturas = Querier.selectFacturas(cliente, facturas);
        this.facturas = facturas;
        this.cliente = cliente;
    }

    public JScrollPane as_panel() {
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;

        // Insertar los encabezados de la tabla
        for (int col = 0; col < encabezados.length; col++) {
            columnas[col].setLayout(new GridBagLayout());
            var prefered_size = encabezados[col].getPreferredSize();
            encabezados[col].setPreferredSize(new Dimension(prefered_size.width + 34, prefered_size.height + 20));
            columnas[col].add(encabezados[col], c);
            // columnas[col].add(, c);
            encabezados[col].setHorizontalAlignment(JLabel.CENTER);
            encabezados[col].setVerticalAlignment(JLabel.CENTER);
            encabezados[col].setFont(new Font(Font.MONOSPACED, Font.BOLD, 12));
            encabezados[col].setForeground(DarkCyan);
            encabezados[col].setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, UI.Violet));
        }

        c.insets = new Insets(2, 2, 2, 2);
        c.fill = GridBagConstraints.NONE;
        c.gridx = 0;
        c.gridy = 1;
        for (Factura factura : this.facturas) {

            // Insertar datos a la fila
            JLabel[] datos = { new JLabel(factura.fecha_as_string()),
                    new JLabel(String.valueOf(factura.numero_factura())),
                    new JLabel(factura.monto_as_string()) };
            c.anchor = GridBagConstraints.CENTER;
            for (int col = 0; col < datos.length; col++) {
                columnas[col].setAlignmentX(1.0f);
                datos[col].setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
                datos[col].setForeground(UI.Black);
                if (col >= 1) {
                    c.anchor = GridBagConstraints.EAST;
                }
                columnas[col].add(datos[col], c);
            }

            // Insertar los montos de la tabla
            c.anchor = GridBagConstraints.EAST;
            boolean populated = false;
            long dias = -ChronoUnit.DAYS.between(LocalDate.now(), factura.fecha());
            c.weightx = 0;
            for (int col = encabezados.length - 1; col >= 3; col--) {
                JLabel label = new JLabel();
                label.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
                if (!populated && dias > rangos[col - 3]) {
                    label.setForeground(DarkCyan);
                    label.setText(factura.monto_as_string());
                    montos[col - 2] = montos[col - 2].add(factura.monto());
                    populated = true;
                } else {
                    label.setText("0.00");
                    label.setForeground(UI.Gray);
                }
                columnas[col].add(label, c);
            }
            montos[0] = montos[0].add(factura.monto());
            c.gridy++;
        }

        JLabel[] summary_labels = { new JLabel("Trab. Final"), new JLabel("Total"),
                new JLabel(montos[0].toString()),
                new JLabel(montos[1].toString()),
                new JLabel(montos[2].toString()),
                new JLabel(montos[3].toString()),
                new JLabel(montos[4].toString()),
                new JLabel(montos[5].toString()) };
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.EAST;
        c.insets = new Insets(4, 0, 4, 0);
        for (JLabel label : summary_labels) {
            label.setHorizontalAlignment(JLabel.RIGHT);
            if (label.getText().equals("0.00")) {
                label.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
                label.setForeground(UI.Gray);
            } else {
                label.setFont(new Font(Font.MONOSPACED, Font.BOLD, 12));
                label.setForeground(DarkGreen);
            }
            label.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, UI.Violet));
        }
        summary_labels[0].setForeground(UI.DarkCyan);
        summary_labels[0].setHorizontalAlignment(JLabel.LEFT);
        summary_labels[1].setForeground(UI.DarkCyan);
        summary_labels[1].setHorizontalAlignment(JLabel.LEFT);

        for (int col = 0; col < summary_labels.length; col++) {
            columnas[col].add(summary_labels[col], c);
        }

        JPanel table_content = new JPanel(new GridBagLayout());
        c.gridy = 0;
        c.gridx = 0;
        c.insets = new Insets(0, 0, 0, 0);

        // Acoplar cada columna a la tabla
        for (JPanel columna : columnas) {
            table_content.add(columna, c);
            var border = BorderFactory.createMatteBorder(2, 0, 2, 1, UI.Violet);
            columna.setBorder(border);
            c.gridx++;
        }
        columnas[0].setBorder(BorderFactory.createMatteBorder(2, 2, 2, 1, UI.Violet));
        columnas[columnas.length - 1].setBorder(BorderFactory.createMatteBorder(2, 0, 2, 2, UI.Violet));
        JScrollPane scrollPane = new JScrollPane(table_content);

        var prefered = scrollPane.getPreferredSize();
        int prefered_height = Math.min(MAX_HEIGHT, prefered.height);
        int minimum_width = (MAX_HEIGHT < prefered.height) ? prefered.width + scrollPane.getVerticalScrollBar().getWidth() : prefered.width;
        scrollPane.setPreferredSize(new Dimension(minimum_width, prefered_height));
        return scrollPane;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Table");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 600);
        Factura[] facturas = Factura.cargar_facturas();
        Cliente[] cliente = Cliente.cargar_clientes();
        // Table table = new Table(Querier.selectFacturas(cliente[15], facturas), cliente[15]);
        Table table = new Table(facturas, cliente[15]);
        frame.add(table.as_panel());
        frame.pack();
        frame.setVisible(true);
    }
}

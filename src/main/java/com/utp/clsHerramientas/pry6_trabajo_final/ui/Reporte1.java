package com.utp.clsHerramientas.pry6_trabajo_final.ui;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.utp.clsHerramientas.pry6_trabajo_final.Cliente;
import com.utp.clsHerramientas.pry6_trabajo_final.Querier;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public final class Reporte1 {
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter
            .ofPattern("dddd 'de dd 'de MMMM 'de yyyy, zzzz");
    final Cliente cliente;
    final Factura[] facturas;

    public Reporte1(Cliente cliente, Factura[] facturas) {
        this.cliente = cliente;
        this.facturas = Querier.selectFacturas(cliente, facturas);
    }

    public JPanel as_panel() {
        JPanel panel = new JPanel(new GridBagLayout());
        var gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(into_header_label("UNIVERSIDAD TECNOLÓGICA DE PANAMÁ"), gbc);
        gbc.gridy = 1;
        panel.add(into_header_label("CENTRO REGIONAL DE CHIRIQUÍ"), gbc);
        gbc.gridy = 2;
        panel.add(into_header_label("FACULTAD DE INGENIERÍA DE SISTEMAS COMPUTACIONALES"), gbc);
        gbc.gridy = 3;
        panel.add(into_header_label("CARRERA: LICENCIATURA EN INGENIERÍA DE SISTEMAS COMPUTACIONALES"), gbc);
        gbc.gridy = 4;
        panel.add(into_header_label("GRUPO:"), gbc);
        gbc.gridy = 5;
        {
            var empty_panel = new JPanel();
            empty_panel.setPreferredSize(new Dimension(50, 50));
            panel.add(empty_panel, gbc);
        }
        gbc.gridy = 6;
        panel.add(into_header_label("ANÁLISIS DE CUENTAS POR COBRAR POR CLIENTE"), gbc);
        gbc.gridy = 7;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(into_label("ESTUDIANTE: ANTONIO NG"), gbc);
        gbc.gridx = 2;
        panel.add(into_label("DOCENTE: Prof. Eduardo Beitia"), gbc);

        gbc.gridy = 8;
        gbc.gridx = 0;
        panel.add(into_label("FECHA: " + FORMATTER.format(LocalDate.now())), gbc);

        gbc.gridy = 9;
        gbc.gridx = 0;

        panel.add(into_label("Codigo de Cliente: <html><p style=\"background-color:MediumBlue;>"
                + cliente.nombre_completo() + "</p></html>"), gbc);
        gbc.gridx = 1;
        panel.add(into_label("nombre: " + cliente.nombre_completo()), gbc);

        Table tabla = new Table(facturas, cliente);
        gbc.gridy = 10;
        gbc.gridx = 0;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(tabla.as_panel(), gbc);
        return panel;
    }

    JLabel into_header_label(String text) {
        JLabel label = new JLabel(text);
        label.setFont(UI.ProjFont.HEADER);
        label.setForeground(UI.DarkGreen);
        return label;
    }

    JLabel into_label(String text) {
        JLabel label = new JLabel(text);
        label.setFont(UI.ProjFont.NORMAL);
        label.setForeground(UI.Black);
        return label;
    }
}

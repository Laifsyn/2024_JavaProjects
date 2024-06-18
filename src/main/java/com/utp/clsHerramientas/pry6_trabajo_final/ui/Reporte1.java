package com.utp.clsHerramientas.pry6_trabajo_final.ui;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.utp.clsHerramientas.pry6_trabajo_final.Cliente;
import com.utp.clsHerramientas.pry6_trabajo_final.Querier;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public final class Reporte1 {
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter
            .ofPattern("EEEE dd 'de' MMMM 'de' yyyy HH:mm:ss a, zzzz",
                    new Locale.Builder().setLanguage("es").setRegion("PA").build());
    final Cliente cliente;
    final Factura[] facturas;
    public final JButton ok = new JButton("OK");

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
        gbc.fill = GridBagConstraints.VERTICAL;
        panel.add(into_header_label("UNIVERSIDAD TECNOLÓGICA DE PANAMÁ"), gbc);
        gbc.gridy = 1;
        panel.add(into_header_label("CENTRO REGIONAL DE CHIRIQUÍ"), gbc);
        gbc.gridy = 2;
        panel.add(into_header_label("FACULTAD DE INGENIERÍA DE SISTEMAS COMPUTACIONALES"), gbc);
        gbc.gridy = 3;
        panel.add(into_header_label("CARRERA: LICENCIATURA EN INGENIERÍA DE SISTEMAS COMPUTACIONALES"), gbc);
        gbc.gridy = 4;
        panel.add(into_header_label("GRUPO: 2IL701"), gbc);
        gbc.gridy = 5;
        panel.add(empty_panel(), gbc);
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
        gbc.gridwidth = 4;
        // panel.add(into_label("FECHA: " + FORMATTER.format(LocalDate.now())), gbc);
        panel.add(into_label("FECHA: " + FORMATTER.format(ZonedDateTime.now())), gbc);

        gbc.gridy = 9;
        gbc.gridx = 0;
        gbc.gridwidth = 2;

        panel.add(into_label("<html><a>Codigo de Cliente: <a style=\"color:Blue;\">"
                + cliente.codigo() + "</a></a></html>"), gbc);
        gbc.gridwidth = 2;
        gbc.gridx = 2;
        panel.add(into_label("             Nombre: " + cliente.nombre_completo()), gbc);

        Table tabla = new Table(facturas, cliente);
        gbc.gridwidth = 2;
        gbc.gridy = 10;
        gbc.gridx = 0;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(this.empty_panel(), gbc);
        gbc.gridy = 11;
        panel.add(tabla.as_panel(), gbc);
        JPanel final_panel = new JPanel();
        final_panel.setLayout(new BoxLayout(final_panel, BoxLayout.Y_AXIS));
        final_panel.add(panel);
        final_panel.add(this.empty_panel());
        final_panel.add(ok);
        return final_panel;
    }

    public void add_ok_action(Runnable action) {
        ok.addActionListener(e -> action.run());
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

    JPanel empty_panel() {
        var empty_panel = new JPanel();
        empty_panel.setPreferredSize(new Dimension(50, 20));
        return empty_panel;
    }

    public static void main(String[] args) {
        Cliente[] cliente = Cliente.cargar_clientes();
        Factura[] facturas = Factura.cargar_facturas();
        Reporte1 reporte = new Reporte1(cliente[3], facturas);
        reporte.add_ok_action(() -> {
            System.out.println("OK! Exiting....");
            System.exit(0);
        });
        UI.show(reporte.as_panel());
    }
}

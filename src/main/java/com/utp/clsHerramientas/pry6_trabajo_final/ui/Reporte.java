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
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

public final class Reporte {
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter
            .ofPattern("EEEE dd 'de' MMMM 'de' yyyy HH:mm:ss a, zzzz",
                    new Locale.Builder().setLanguage("es").setRegion("PA").build());
    final Cliente cliente;
    final Factura[] facturas;
    public final JButton ok = new JButton("OK");
    static public final int[] dias = { 10000, 120, 90, 60, 30, 0 };

    public Reporte(Cliente cliente, Factura[] facturas) {
        this.cliente = cliente;
        this.facturas = Querier.selectFacturas(cliente, facturas);
    }

    public JPanel as_reporte_1() {

        JPanel final_panel = new JPanel(new GridBagLayout());
        Table tabla = new Table(facturas, cliente);

        var gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;

        final_panel.add(this.encabezado(), gbc);
        gbc.gridy = 1;
        final_panel.add(this.empty_panel(), gbc);
        gbc.gridy = 2;
        final_panel.add(tabla.as_panel(), gbc);
        gbc.gridy = 3;
        final_panel.add(this.empty_panel(), gbc);
        gbc.gridy = 4;
        final_panel.add(ok, gbc);

        return final_panel;
    }

    public JPanel as_reporte_2() {

        JPanel final_panel = new JPanel(new GridBagLayout());

        var gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;

        final_panel.add(this.encabezado(), gbc);
        gbc.gridy = 1;
        // RESUMEN FINAL DE CUENTAS POR COBRAR/CLIENTES
        final_panel.add(this.empty_panel(), gbc);
        gbc.gridy = 2;
        final_panel.add(into_label("RESUMEN FINAL DE CUENTAS POR COBRAR/CLIENTES"), gbc);
        gbc.gridy = 3;
        final_panel.add(this.empty_panel());
        gbc.gridy = 4;

        JPanel resumen = new JPanel(new GridBagLayout());
        JLabel[] labels = { into_label("MES CORRIENTE: "),
                into_label("30 DIAS: "),
                into_label("60 DIAS: "),
                into_label("90 DIAS: "),
                into_label("+120 DIAS: ") };
        var resumen_gbc = new GridBagConstraints();
        for (int i = 1; i < labels.length + 1; i++) {
            resumen_gbc.gridx = 0;
            resumen_gbc.gridy = i - 1;
            resumen.add(labels[i - 1], resumen_gbc);
            resumen_gbc.gridx = 1;
            // some offset due to dias[] layout
            BigDecimal summary = summary(labels.length - i + 1);
            JLabel monto;
            if (summary.equals(BigDecimal.ZERO)) {
                monto = into_label("0.00");
            } else {
                monto = into_label(summary.toString());
            }
            if (i == 1) {
                monto.setForeground(UI.DarkCyan);

            }
            resumen_gbc.anchor = GridBagConstraints.EAST;
            resumen.add(monto, resumen_gbc);
        }
        final_panel.add(resumen, gbc);
        gbc.gridy = 5;
        final_panel.add(this.empty_panel(), gbc);
        gbc.gridy = 6;

        // Table tabla = new Table(facturas, cliente);
        // final_panel.add(tabla.as_panel(), gbc);
        // gbc.gridy = 7;
        final_panel.add(ok, gbc);
        return final_panel;
    }

    public BigDecimal summary(int idx_dia) {
        BigDecimal resumen = BigDecimal.ZERO;
        for (int i = 0; i < facturas.length; i++) {
            long antiguedad = -facturas[i].antiguedad();
            if (antiguedad >= dias[idx_dia] && antiguedad < dias[idx_dia - 1]) {
                resumen = resumen.add(facturas[i].monto());
            }
            // System.out.printf("%d)Antiguedad: %d, monto: %s (%s)\n", idx_dia, antiguedad, facturas[i].monto(),
            //         (antiguedad >= dias[idx_dia] && antiguedad < dias[idx_dia - 1]));
        }
        resumen.setScale(2, RoundingMode.HALF_EVEN);
        return resumen;
    }

    public JPanel encabezado() {

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

        return panel;
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
        Reporte reporte = new Reporte(cliente[4], facturas);
        reporte.add_ok_action(() -> {
            System.out.println("OK! Exiting....");
            System.exit(0);
        });
        UI.show(reporte.as_reporte_2());
    }
}

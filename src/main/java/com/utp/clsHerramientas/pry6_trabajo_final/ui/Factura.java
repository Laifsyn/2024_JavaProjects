package com.utp.clsHerramientas.pry6_trabajo_final.ui;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

import com.utp.clsHerramientas.pry6_trabajo_final.Datos;

public record Factura(String codigo, int numero_factura, LocalDate fecha, BigDecimal monto)
        implements Comparable<Factura> {

    public static final Integer FACTURAS_CLIENTE_LEN = 60;
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    @Override
    public int compareTo(Factura o) {
        return codigo.compareTo(o.codigo);
    }

    public Factura(String codigo, int numero_factura, String fecha, String monto) {
        this(codigo, numero_factura, LocalDate.parse(fecha, FORMATTER),
                new BigDecimal(monto, new MathContext(24, RoundingMode.HALF_EVEN)));
    }

    public String fecha_as_string() {
        return fecha.format(FORMATTER);
    }

    public String monto_as_string() {
        var format = NumberFormat.getInstance();
        format.setMinimumFractionDigits(2);
        return format.format(monto);
    }
    public long antiguedad(){
        return ChronoUnit.DAYS.between(LocalDate.now(), fecha);
    }

    public static void main(String[] args) {
        Factura[] facturas = cargar_facturas();
        for (int i = 0; i < facturas.length; i++) {
            System.out.println(i + "-)" + facturas[i]);
        }
        System.out.println(facturas[58].monto_as_string());
    }

    public static Factura[] cargar_facturas() {
        Factura[] facturas = new Factura[FACTURAS_CLIENTE_LEN];
        String[] datosFac = new String[FACTURAS_CLIENTE_LEN];
        Datos.facturasCliente(datosFac);
        for (int i = 0; i < FACTURAS_CLIENTE_LEN; i++) {
            String[] datos = datosFac[i].split(" +");
            facturas[i] = new Factura(datos[0], Integer.parseInt(datos[1]), datos[2], datos[3]);
        }
        Arrays.sort(facturas);
        return facturas;
    }
}

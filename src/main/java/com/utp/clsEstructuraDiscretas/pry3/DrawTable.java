package com.utp.clsEstructuraDiscretas.pry3;

import java.util.ArrayList;

public class DrawTable {
    private int cantidad_columnas = 0;
    private ArrayList<String[]> table;

    public DrawTable() {
        this.table = new ArrayList<>();
    }

    public void insertar_fila(String[] row) {
        if (this.cantidad_columnas == 0) {
            this.cantidad_columnas = row.length;
        }
        // Todas las filas tienen que tener la misma cantidad columna
        if (this.cantidad_columnas != row.length) {
            throw new IllegalArgumentException("La fila no tiene la misma cantidad de columnas que las anteriores");
        }
        this.table.add(row);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        // Guardo la posicion de los vertices de la tabla.
        Integer[] columns_widths = new Integer[this.cantidad_columnas];

        // Obtener los vertices de la tabla
        for (int indice_columna = 0; indice_columna < this.cantidad_columnas; indice_columna++) {
            int ancho_columna = 0;
            for (String[] fila : this.table) {
                String celda = fila[indice_columna];
                if (celda.length() >= ancho_columna) {
                    // +1 para el caracter que encierra la celda
                    ancho_columna = celda.length() + 1;
                    columns_widths[indice_columna] = ancho_columna;
                }
            }
        }

        StringBuilder borde_superior = new StringBuilder("┌");
        StringBuilder separador_fila = new StringBuilder("├");
        StringBuilder borde_inferior = new StringBuilder("└");
        // Construimos los separadores
        for (int indice_columna = 0; indice_columna < this.cantidad_columnas; indice_columna++) {
            int padding = columns_widths[indice_columna];
            // El ultimo caracter se encierra con algo distinto.

            borde_superior.append(String.format("%" + padding + "s", "┬").replace(" ", "─"));
            separador_fila.append(String.format("%" + padding + "s", "┼").replace(" ", "─"));
            borde_inferior.append(String.format("%" + padding + "s", "┴").replace(" ", "─"));
            if (indice_columna == this.cantidad_columnas - 1) {
                borde_superior.setLength(borde_superior.length() - 1);
                separador_fila.setLength(separador_fila.length() - 1);
                borde_inferior.setLength(borde_inferior.length() - 1);
            }
        }
        borde_superior.append("┐\n");
        separador_fila.append("┤\n");
        borde_inferior.append("┘\n");

        sb.append(borde_superior);
        char barra_vertical = '│';
        // Usamos Vanilla For para poder insertar separador de fila despues del
        // encabezado
        for (int indice_fila = 0; indice_fila < this.table.size(); indice_fila++) {
            sb.append(barra_vertical);
            for (int indice_columna = 0; indice_columna < this.cantidad_columnas; indice_columna++) {
                String celda = this.table.get(indice_fila)[indice_columna];
                int padding = columns_widths[indice_columna];
                sb.append(String.format("%" + padding + "s", celda + barra_vertical));
            }
            sb.append("\n");
            if (indice_fila == 0) {
                sb.append(separador_fila);
            }
        }
        sb.append(borde_inferior);
        return sb.toString();
    }

    public static void main(String[] args) {

        String[][] mi_tabla = new String[][] { { "Hola mundo", "asdfa", "Nostradamus" },
                { "1", "23jgadsdsavghgh", "3" },
                { "4", "5", "6" } };
        DrawTable table = new DrawTable();
        for (String[] fila : mi_tabla)
            table.insertar_fila(fila);
        System.out.println(table.toString());
        System.exit(0);
    }
}

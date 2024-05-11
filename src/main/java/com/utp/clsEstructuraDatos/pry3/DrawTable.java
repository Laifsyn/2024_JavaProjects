package com.utp.clsEstructuraDatos.pry3;

import java.util.ArrayList;

public class DrawTable {
    private int columnas = 0;
    private ArrayList<String[]> table = null;

    public DrawTable() {
    }

    public void insertar_fila(String[] row) {
        if (this.columnas == 0) {
            this.columnas = row.length;
        }
        // Todas las filas tienen que tener la misma cantidad columna
        if (this.columnas != row.length) {
            throw new IllegalArgumentException("La fila no tiene la misma cantidad de columnas que las anteriores");
        }
        this.table.add(row);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        throw new UnsupportedOperationException("asdasdsa");
        // for (String[] row : this.table) {
        // for (String cell : row) {
        // sb.append(cell);
        // sb.append(" ");
        // }
        // sb.append("\n");
        // }
        // return sb.toString();
    }

    /*
     * |__|| asdfa||c|
     * ------------------------
     * | 1||23jgadsdsavghgh||3|
     * | 4||              5||6|
     */
    public static void main(String[] args) {

        String[][] mi_tabla = new String[][] { { "____", "asdfa", "c" }, { "1", "23jgadsdsavghgh", "3" },
                { "4", "5", "6" } };
        ArrayList<Integer> ancho_columna = new ArrayList<>();
        System.err.println("Antes  : " + ancho_columna.toString());
        for (String celda : mi_tabla[0]) {
            ancho_columna.add(celda.length());
        }

        System.out.println("Primera vuelta te va a dar: " + ancho_columna.toString());
        for (String[] fila : mi_tabla) {
            for (int i = 0; i < fila.length; i++) {
                if (ancho_columna.get(i) < fila[i].length()) {
                    ancho_columna.set(i, fila[i].length());
                }
            }
        }

        System.out.println();
        System.err.println("Despues: " + ancho_columna.toString());

        System.out.println("Tabla de verdad");
        for (int j = 0; j < mi_tabla.length; j++) {
            for (int i = 0; i < mi_tabla[j].length; i++) {
                // Por defecto
                System.out.print(String.format("|%" + ancho_columna.get(i) + "s|", mi_tabla[j][i]));
            }
            System.out.println();
           
            if (j == 0) {
                for (int k = 0; k < mi_tabla[0].length; k++) {
                    System.out.print("|" + "=".repeat(ancho_columna.get(k)) + "|");
                }
                System.out.println();
            }
        }
        System.exit(0);
    }
}
    
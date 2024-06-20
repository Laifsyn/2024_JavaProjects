package com.utp.clsEstructuraDatos.Estructuras;

import java.util.ArrayList;

public class Pila<T> {
    public static void main(String[] args) {
        Pila<Integer> pila = new Pila<>(10);
        pila.insertar(10);
        pila.insertar(10);
        pila.insertar(1);
        pila.insertar(-415);
        pila.insertar(1104);
        pila.insertar(110);
        pila.insertar(141);
        pila.insertar(4142);
        pila.insertar(11242);
        pila.insertar(1414214);
        System.out.println("Cima: " + pila.getCima());
        System.out.println(pila.imprimirPila());
        pila.quitarCima();
        System.out.println("Quitar cima");
        System.out.println(pila.imprimirPila());
        pila.insertar(6);
        System.out.println("Insertar 6");
        System.out.println(pila.imprimirPila());
        // pila.limpiarPila();
        // System.out.println("Limpiar pila");
        // System.out.println(pila.imprimirPila());
    }

    private int cima = 0;
    private final int capacidad;
    private ArrayList<T> inner;

    public Pila(int capacidad) {
        this.capacidad = capacidad;
        this.inner = new ArrayList<>(capacidad);
    }

    public boolean insertar(T dato) {
        if (this.cima == this.capacidad) {
            return false;
        }

        // Nunca limpiamos el arreglo, asi que al insertar tenemos que reemplazar el
        // indice si existe
        if (this.cima < this.inner.size()) {
            this.inner.set(this.cima, dato);
        } else {
            this.inner.add(dato);
        }
        this.cima++;
        return true;
    }

    public void limpiarPila() {
        this.cima = 0;
    }

    public T quitarCima() {
        if (this.cima == 0) {
            return null;
        }
        this.cima--;
        return this.inner.remove(this.cima);
    }

    public T verCima() {
        if (this.cima == 0) {
            return null;
        }
        return this.inner.get(this.cima - 1);
    }

    public boolean pilaVacia() {
        return this.cima == 0;
    }

    public boolean pilaLLena() {
        return this.cima == this.capacidad;
    }

    // public String impresion_sencilla() {
    // StringBuilder sb = new StringBuilder();
    // Integer index_width = 0;
    // int cima = this.cima;
    // while (cima > 0) {
    // cima /= 10;
    // index_width++;
    // }
    // String encabezado_indice = "Indice";
    // String encabezado_valores = "Valores";

    // if (encabezado_indice.length() > index_width) {
    // index_width = encabezado_indice.length();
    // }
    // int cell_width = encabezado_valores.length();
    // for (T cell : this.inner) {
    // int cell_length = cell.toString().length();
    // if (cell_length > cell_width) {
    // cell_width = cell_length;
    // }
    // }

    // String encabezado = String.format("| %" + index_width + "s | %" + cell_width
    // + "s |\n", encabezado_indice,
    // encabezado_valores);
    // sb.append(encabezado);
    // sb.append(String.format("├%" + (encabezado.length() - 3) + "s┤\n",
    // "").replace(" ", "─"));
    // for (int i = 1; i <= this.cima; i++) {
    // String cell = this.inner.get(i - 1).toString();
    // sb.append(String.format("| %" + index_width + "s | ", i));
    // sb.append(String.format("%" + cell_width + "s |", cell));
    // sb.append("\n");
    // }
    // return sb.toString();
    // }

    public String imprimirPila() {
        // Imprimimos de cima a base de la pila.
        DrawTable tabla = new DrawTable();

        for (int indice = this.cima; indice > 0; indice--) {
            tabla.insertar_fila(new String[] { String.valueOf(indice), this.inner.get(indice - 1).toString() });
        }

        tabla.insertar_fila(new String[] { "Indice", "Valores" });
        return tabla.toString();
    }

    public int getCima() {
        return this.cima;
    }

    public int getCapacidad() {
        return this.capacidad;
    }

}

class DrawTable {
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
            if (indice_fila == this.table.size() - 2) {
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

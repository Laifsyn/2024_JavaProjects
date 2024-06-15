package com.utp.clsEstructuraDatos.Estructuras.colas;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;

public abstract class AbstractCola<T> {
    /**
     * Indice donde se puede extraer el siguiente elemento
     */
    int frente = 0;
    /**
     * Indice donde se puede insertar el siguiente elemento
     */
    int cola = 0;
    /**
     * Cantidad de elementos en la cola
     */
    int longitud = 0;
    /**
     * Capacidad máxima de la cola
     */
    int capacidad;
    /**
     * Arreglo que contiene los elementos de la cola
     */
    final T[] inner;

    @SuppressWarnings("unchecked")
    public AbstractCola(int capacidad) {
        this.capacidad = capacidad;
        this.inner = (T[]) new Object[capacidad];
    }

    public int capacity() {
        return this.capacidad;
    }

    /**
     * Evalua si la cola está vacía al comparar la longitud con 0
     */
    public boolean isEmpty() {
        return this.longitud == 0;
    }

    /**
     * Evalua si la cola está llena al comparar la longitud con la capacidad
     */
    public boolean isFull() {
        return this.longitud == this.capacidad;
    }

    /**
     * Limpia la cola y la deja vacía, y reinicia la cola.
     */
    public Result.ColaVacia limpiar() {
        for (int i = 0; i < capacidad; i++) {
            this.inner[i] = null;
        }
        this.reset();
        return new Result.ColaVacia(capacidad);
    }

    /**
     * Reinicia los indices de frente y cola, y la longitud de la cola
     */
    public void reset() {
        this.frente = 0;
        this.cola = 0;
        this.longitud = 0;
    }

    /**
     * Devuelve una representación opinionada de la cola en forma de cadena
     */
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < capacidad; i++) {
            T elemento = inner[i];
            if (elemento == null)
                sb.append("_");
            else
                sb.append(elemento);
            sb.append(", ");
        }

        // Remueve la última coma
        sb.delete(sb.length() - 2, sb.length());
        sb.append("]");
        return sb.toString();
    }

    public JPanel as_Jpanel() {
        return new PanelDrawer(this).as_panel();
    }

    private class PanelDrawer {
        final JPanel panel = new JPanel(new GridBagLayout());
        final AbstractCola<T> cola;
        final JLabel front_surplus = new JLabel();
        final JLabel frente_item = new JLabel();
        final JLabel middle = new JLabel();
        final JLabel cola_item = new JLabel();
        final JLabel cola_surplus = new JLabel();

        /**
         * ?["{frente-1}"]
         * ["{this[frente]}"]
         * [..."{longitud}"...]
         * ["...{this[cola-1]}]
         * ?[cola-1 - capacidad]
         */
        PanelDrawer(AbstractCola<T> cola) {
            this.cola = cola;
            var c = new GridBagConstraints(0, 0, 1, 5, 0, 0, GridBagConstraints.CENTER, 0, new Insets(5, 5, 5, 5), 0,
                    0);
            panel.add(front_surplus, c);
            c.gridy = 1;
            panel.add(frente_item, c);
            c.gridy = 2;
            panel.add(middle, c);
            c.gridy = 3;
            panel.add(cola_item, c);
            c.gridy = 4;
            panel.add(cola_surplus, c);
        }

        JPanel as_panel() {
            int frente = this.cola.frente;
            int cola = this.cola.cola;
            int capacidad = this.cola.capacity();
            int longitud = this.cola.longitud;

            // ******************************************

            if (frente > cola)
                wrapped(frente, cola, capacidad, longitud);
            else
                aligned(frente, cola, capacidad, longitud);

            // ******************************************
            return this.panel;
        }

        /**
         * llamado cuando frente (donde se retira de la fila) es menor o igual que el
         * indice de la cola
         */
        void aligned(int frente, int cola, int capacidad, int longitud) {

            if (frente > 0)
                this.front_surplus.setText("[" + (frente - 1) + "]");
            else
                this.front_surplus.setText("");

            // ******************************************

            this.frente_item.setText("[" + this.cola.inner[frente] + "]");
            // ******************************************

            if (longitud > 0)
                this.middle.setText("[..." + longitud + "...]");
            else
                this.middle.setText("[0]");

            // ******************************************
            this.cola_item.setText("[" + this.cola.inner[cola] + "]");

            // ******************************************

            if (cola < capacidad - 1)
                this.cola_surplus.setText("[" + (cola + 1 - capacidad) + "]");
            else
                this.cola_surplus.setText("");
        }

        /**
         * llamado cuando frente (donde se retira de la fila) es mayor que el indice de
         * la cola
         */
        void wrapped(int frente, int cola, int capacidad, int longitud) {

            this.front_surplus.setText("(" + frente + ")");

            // ******************************************

            this.frente_item.setText("[" + this.cola.inner[frente] + "]");

            // ******************************************

            if (longitud > 0)
                this.middle.setText("[..." + longitud + "...]");
            else
                this.middle.setText("[0]");

            // ******************************************
            this.cola_item.setText("[" + this.cola.inner[cola] + "]");

            // ******************************************

            this.cola_surplus.setText("[" + (cola + 1 - capacidad) + "]");
        }

    }

// @formatter:off
    abstract public Result insertar(T elemento);
    abstract public Option<T> quitar();
    
    public static sealed interface Result {
        public record OK() implements Result {}
        public record ColaLlena() implements Result {}
        public record ColaVacia(int size_hint) implements Result {}
// @formatter:on
    }

// @formatter:off
    public static sealed interface Option<T> {
        public record Some<T>(T value) implements Option<T> {}
        public record None<T>() implements Option<T> {}
    }
// @formatter:on
}

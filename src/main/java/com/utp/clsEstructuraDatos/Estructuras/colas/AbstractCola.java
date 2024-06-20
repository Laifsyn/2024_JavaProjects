package com.utp.clsEstructuraDatos.Estructuras.colas;

import java.awt.Color;
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

    /** Cantidad de elementos accesibles en la cola */
    public int len() {
        return this.longitud;
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

    public PanelDrawer as_drawer() {
        return new PanelDrawer(this);
    }

    public class PanelDrawer {
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
        }

        public int frente() {
            return this.cola.frente;
        }

        public int cola() {
            return this.cola.cola;
        }

        public int longitud() {
            return this.cola.longitud;
        }

        public int capacity() {
            return this.cola.capacity();
        }

        public JPanel as_panel() {
            JPanel panel = new JPanel(new GridBagLayout());
            var c = new GridBagConstraints();
            c.insets = new Insets(2, 5, 5, 5);
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx = 0;
            c.gridy = 0;

            panel.add(front_surplus, c);
            c.gridy = 1;
            panel.add(frente_item, c);
            c.gridy = 2;
            panel.add(middle, c);
            c.gridy = 3;
            panel.add(cola_item, c);
            c.gridy = 4;
            panel.add(cola_surplus, c);

            int frente = this.cola.frente;
            int cola = this.cola.cola;

            // ******************************************

            if (frente > cola)
                wrapped();
            else
                aligned();

            // ******************************************
            return panel;
        }

        /**
         * llamado cuando frente (donde se retira de la fila) es menor o igual que el
         * indice de la cola
         */
        public void aligned() {
            int frente = this.frente();
            if (frente > 0) {
                this.front_surplus.setText("[+" + (frente) + "...]");
                this.front_surplus.setForeground(Color.BLACK);
                this.front_surplus.setToolTipText("Espacio libre antes del frente");
            } else {
                this.front_surplus.setText("");
                this.front_surplus.setToolTipText(null);
            }
            update_content();

            int capacidad = this.capacity();
            if (this.longitud() < capacidad) {
                this.cola_surplus.setText("[...+" + (capacidad - this.cola()) + "]");
                this.cola_surplus.setForeground(Color.BLACK);
                this.cola_surplus.setToolTipText("Espacio libre para inserción");
            } else {
                this.cola_surplus.setText("[Full]");
                this.cola_surplus.setForeground(Color.RED);
                this.cola_surplus.setToolTipText(null);
            }
        }

        /**
         * llamado cuando frente (donde se retira de la fila) es mayor que el indice de
         * la cola
         */
        public void wrapped() {
            int frente = this.frente();
            this.front_surplus.setText("Frente idx: (" + frente + ")");
            this.front_surplus.setToolTipText("Indice de frente");
            update_content();

            if (this.longitud() < this.capacity()) {
                this.cola_surplus.setText("[...+" + (frente - this.cola()) + "]");
                this.cola_surplus.setForeground(Color.BLACK);
                this.cola_surplus.setToolTipText("Espacio libre para inserción");
            } else {
                this.cola_surplus.setText("[Full]");
                this.cola_surplus.setForeground(Color.RED);
                this.cola_surplus.setToolTipText(null);
            }
        }

        void update_content() {

            // ******************************************

            if (longitud > 0) {
                this.frente_item.setText("Frente: [" + this.cola.inner[frente] + "]");
                this.frente_item.setForeground(Color.BLACK);
                this.frente_item.setToolTipText("Elemento en la posición de frente");
            } else {
                this.frente_item.setText("Frente: [_EMPTY_]");
                this.frente_item.setForeground(Color.RED);
                this.frente_item.setToolTipText("La cola está vacía");
            }
            // ******************************************

            if (longitud > 0) {
                this.middle.setText("Longitud: [..." + longitud + "...]");
                this.middle.setForeground(Color.BLACK);
                this.middle.setToolTipText("Cantidad de elementos en la cola");
            } else {
                this.middle.setText("Longitud: [0]");
                this.middle.setForeground(Color.RED);
                this.middle.setToolTipText("No hay elementos en la cola");
            }

            // ******************************************
            int cola_index_item = (this.cola() - 1) % capacidad;
            if (cola_index_item < 0) {
                cola_index_item += capacidad;
            }
            if (longitud > 0) {
                this.cola_item.setText("Cola: [" + this.cola.inner[cola_index_item] + "]");
                this.cola_item.setForeground(Color.BLACK);
                this.cola_item.setToolTipText("Elemento en la posición de cola");
            } else {
                this.cola_item.setText("Cola: [_EMPTY_]");
                this.cola_item.setForeground(Color.RED);
                this.cola_item.setToolTipText("La cola está vacía");
            }

            // ******************************************
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

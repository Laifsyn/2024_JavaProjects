package com.utp.clsEstructuraDatos.Estructuras.colas;

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

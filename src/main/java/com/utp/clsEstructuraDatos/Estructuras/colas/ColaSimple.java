package com.utp.clsEstructuraDatos.Estructuras.colas;

public class ColaSimple<T> extends AbstractCola<T> {
    /**
     * <p>
     * Una <b>`ColaSimple`</b> difiere de una circular, en que la longitud (lo que
     * indica si
     * la cola está vacía o llena) no se reinicia hasta que no hayan elementos en la
     * cola.
     * </p>
     * 
     * <p>
     * Esto quiere decir que antes de que se reinicie la longitud, lo más que se
     * puede insertar o quitar son <b>`ColaSimple.capacidad`</b> elementos
     * </p>
     * 
     * @param capacidad
     *            int
     */
    public ColaSimple(int capacidad) {
        super(capacidad);
    }

    /**
     * Inserta un elemento en la cola.
     * Condicion de llenado: Cuando la longitud de la cola es igual a la capacidad.
     */
    @Override
    public Result insertar(T elemento) {
        if (this.isFull())
            return new Result.ColaLlena();

        this.inner[cola] = elemento;

        this.cola = (cola + 1) % capacidad;
        this.longitud++;
        return new Result.OK();
    }

    /**
     * Quita un elemento de la cola, si la cola está vacía retorna una cola vacía.
     */
    @Override
    public Option<T> quitar() {
        if (this.isEmpty())
            return new Option.None<>();

        T elemento = this.inner[frente];
        this.inner[frente] = null;
        frente = (frente + 1) % capacidad;
        if (frente == cola)
            this.reset();
        return new Option.Some<T>(elemento);
    }

}
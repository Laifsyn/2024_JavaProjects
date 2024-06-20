package com.utp.clsEstructuraDatos.Estructuras.colas;

public class ColaCircular<T> extends AbstractCola<T> {
    public ColaCircular(int capacidad) {
        super(capacidad);
    }

    @Override
    public Result insertar(T elemento) {
        if (this.isFull())
            return new Result.ColaLlena();

        this.inner[cola] = elemento;
        
        this.cola = (cola + 1) % capacidad;
        this.longitud++;
        return new Result.OK();
    }

    @Override
    public Option<T> quitar() {
        if (this.isEmpty())
            return new Option.None<>();

        T elemento = this.inner[frente];
        inner[frente] = null;

        frente = (frente + 1) % capacidad;
        longitud--;
        return new Option.Some<>(elemento);
    }
}

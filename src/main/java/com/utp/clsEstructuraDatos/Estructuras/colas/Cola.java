package com.utp.clsEstructuraDatos.Estructuras.colas;

public class Cola<T> {
    public static void main(String[] args) {
        Cola<Long> cola = new Cola<>(5);
        cola.insertar(80l);
        cola.insertar(568l);
        cola.insertar(466651l);
        cola.insertar(46512365123512312l);
        cola.insertar(5484864856456553111l);
        System.out.println(cola.toString());
        cola.quitar_frente();
        System.out.println(cola.toString());

    }

    // Posición en la que se puede extraer
    int frente = 0;
    // Posición en la que se puede insertar
    int cola = 0;
    int longitud = 0;
    int capacidad;
    final Object[] inner;

    public Cola(int capacidad) {
        this.capacidad = capacidad;
        this.inner = new Object[capacidad];
    }

    boolean insertar(T elemento) {
        if (is_full())
            return false;
        inner[cola] = elemento;
        System.out.println((T) elemento);
        cola++;
        if (cola == capacidad)
            cola = 0;

        longitud++;
        return true;
    }

    T quitar_frente() {
        if (longitud == 0)
            return null;
        var frente = this.frente;
        longitud--;
        this.frente++;
        if (this.frente == capacidad)
            this.frente = 0;
        @SuppressWarnings("unchecked")
        var valor = (T) inner[frente];
        inner[frente] = null;
        return valor;
    }

    int espacio_libre() {
        return capacidad - longitud;
    }

    boolean is_full() {
        return !(longitud < capacidad);
    }

    boolean is_empty() {
        return (longitud == 0);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (var item : inner) {
            if (item == null)
                sb.append("_, ");
            else
                sb.append(String.format("%s, ", item));
        }
        sb.delete(sb.length() - 2, sb.length());
        sb.append("]");
        return sb.toString();
    }
}

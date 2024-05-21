package com.utp.clsEstructuraDatos.Estructuras;

import java.util.ArrayList;

public class Pila<T> {
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

    public String imprimirPila() {
        StringBuilder sb = new StringBuilder();
        int width = 0;
        int cima = this.cima;
        while ((cima /= 10) > 0) {
            width++;
        }
        for (int i = 0; i < this.cima; i++) {
            sb.append(String.format("| %0" + width + "s | ", i));
            sb.append(this.inner.get(i).toString());
            sb.append("\n");
        }
        return sb.toString();
    }

    public int getCima() {
        return this.cima;
    }

    public int getCapacidad() {
        return this.capacidad;
    }

}

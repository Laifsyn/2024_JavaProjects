package com.utp.clsEstructuraDatos.colas;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class colasproy {
    public static void main(String[] args) {
        new TerminalColasApp().run();
    }
}

class TerminalColasApp {
    private AbstractCola<Integer> cola;
    private Scanner scanner = new Scanner(System.in);

    public void run() {
        while (true) {
            showMenu();
            int choice = getUserInputInt("Seleccione una opción: ");
            switch (choice) {
                case 1 -> seleccionarTipoCola();
                case 2 -> insertar();
                case 3 -> quitar();
                case 4 -> {
                    System.out.println("Saliendo...");
                    return;
                }
                default -> System.out.println("Opción no válida.");
            }
            mostrar();
        }
    }

    private void showMenu() {
        System.out.println("\n1. Seleccionar tipo de cola (circular o estándar)");
        System.out.println("2. Insertar elemento");
        System.out.println("3. Quitar elemento");
        System.out.println("4. Salir");
    }

    private void seleccionarTipoCola() {
        int capacidad = getUserInputInt("Ingrese la capacidad de la cola: ");
        int tipo = getUserInputInt("1. Cola Circular\n2. Cola Estándar\nSeleccione el tipo de cola: ");
        cola = (tipo == 1) ? new ColaCircular<>(capacidad) : new ColaSimple<>(capacidad);
        System.out.println("Cola creada: " + (tipo == 1 ? "Circular" : "Estándar"));
    }

    private void insertar() {
        if (cola == null) {
            System.out.println("Primero debe seleccionar el tipo de cola.");
            return;
        }
        int elemento = getUserInputInt("Ingrese el elemento a insertar: ");
        try {
            cola.insertar(elemento);
            System.out.println("Elemento insertado: " + elemento);
        } catch (IllegalStateException e) {
            System.out.println("Error: La cola está llena.");
        }
    }

    private void quitar() {
        if (cola == null) {
            System.out.println("Primero debe seleccionar el tipo de cola.");
            return;
        }
        try {
            int elemento = cola.quitar();
            System.out.println("Elemento quitado: " + elemento);
        } catch (IllegalStateException e) {
            System.out.println("Error: La cola está vacía.");
        }
    }

    private void mostrar() {
        if (cola != null) {
            System.out.println("Estado de la cola: " + cola);
        }
    }

    private int getUserInputInt(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.print("Entrada no válida. " + prompt);
            scanner.next();
        }
        return scanner.nextInt();
    }
}

// Estructuras de datos (ColaSimple y ColaCircular)
abstract class AbstractCola<T> {
    protected int capacidad, longitud;

    public AbstractCola(int capacidad) {
        this.capacidad = capacidad;
    }

    public int capacity() {
        return capacidad;
    }

    public int len() {
        return longitud;
    }

    public abstract void insertar(T elemento);

    public abstract T quitar();

    public abstract void limpiar();

    @Override
    public abstract String toString();
}

class ColaSimple<T> extends AbstractCola<T> {
    private Queue<T> cola = new LinkedList<>();

    public ColaSimple(int capacidad) {
        super(capacidad);
    }

    @Override
    public void insertar(T elemento) {
        if (longitud >= capacidad)
            throw new IllegalStateException();
        cola.add(elemento);
        longitud++;
    }

    @Override
    public T quitar() {
        if (longitud <= 0)
            throw new IllegalStateException();
        longitud--;
        return cola.poll();
    }

    @Override
    public void limpiar() {
        cola.clear();
        longitud = 0;
    }

    @Override
    public String toString() {
        return cola.toString();
    }
}

class ColaCircular<T> extends AbstractCola<T> {
    private T[] cola;
    private int frente = 0, finalCola = 0;

    @SuppressWarnings("unchecked")
    public ColaCircular(int capacidad) {
        super(capacidad);
        this.cola = (T[]) new Object[capacidad];
    }

    @Override
    public void insertar(T elemento) {
        if (longitud >= capacidad)
            throw new IllegalStateException();
        cola[finalCola] = elemento;
        finalCola = (finalCola + 1) % capacidad;
        longitud++;
    }

    @Override
    public T quitar() {
        if (longitud <= 0)
            throw new IllegalStateException();
        T elemento = cola[frente];
        frente = (frente + 1) % capacidad;
        longitud--;
        return elemento;
    }

    @Override
    public void limpiar() {
        frente = finalCola = longitud = 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < longitud; i++) {
            sb.append(cola[(frente + i) % capacidad]);
            if (i < longitud - 1)
                sb.append(", ");
        }
        return sb.append("]").toString();
    }
}

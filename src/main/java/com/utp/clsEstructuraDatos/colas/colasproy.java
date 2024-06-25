package com.utp.clsEstructuraDatos.colas;

import java.util.Scanner;

public class colasproy {
    // Cola Lineal
    private static int[] colaLineal = new int[5];
    private static int frenteLineal = -1;
    private static int finalColaLineal = -1;
    private static final int capacidadLineal = 5;

    private static void contenidoColaLineal() // MOSTRAR CONTENIDO DE LA COLA
    {
        System.out.println("Contenido de la cola lineal: ");
        if (frenteLineal == -1) {
            System.out.println("La cola está vacía");
        } else {
            System.out.print("[ ");
            for (int i = frenteLineal; i <= finalColaLineal; i++) {
                System.out.print(colaLineal[i] + " ");
            }
            System.out.println("]");
        }
    }

    private static void insercionColaLineal(int num) // INSERCIÓN DE CONTENIDO A LA COLA
    {
        System.out.println("\nInserción de contenido en la cola lineal:");
        System.out.println("-------------------------------");
        System.out.println("Estado de la cola antes de la inserción:");
        contenidoColaLineal();

        if (frenteLineal == -1)
            frenteLineal = 0;

        colaLineal[++finalColaLineal] = num;

        System.out.println("\nEl número " + num + " ha sido insertado en la cola lineal.");
        System.out.println("Estado de la cola después de la inserción:");
        contenidoColaLineal();
    }

    private static void eliminacionColaLineal() // ELIMINACIÓN DE CONTENIDO EN LA COLA
    {
        System.out.println("\nEliminación de contenido en la cola lineal:");
        System.out.println("------------------------------------------");
        if (!colaLinealVacia()) {
            int numEliminado = colaLineal[frenteLineal++];
            if (frenteLineal > finalColaLineal) {
                frenteLineal = finalColaLineal = -1;
            }
            System.out.println("El número eliminado es: " + numEliminado);
            System.out.println("Estado de la cola después de la eliminación:");
            contenidoColaLineal();
        } else {
            System.out.println("La cola está vacía. No se puede eliminar ningún elemento.");
        }
    }

    private static boolean colaLinealVacia() {
        return frenteLineal == -1;
    }

    private static boolean colaLinealLlena() {
        return finalColaLineal == capacidadLineal - 1;
    }

    private static int frenteColaLineal() {
        return colaLineal[frenteLineal];
    }

    // COLA CIRCULAR
    private static final int capacidadCircular = 5;
    private static int[] colaCircular = new int[capacidadCircular];
    private static int frenteCircular = 0;
    private static int finalColaCircular = -1;
    private static int sizeCircular = 0;

    private static void contenidoColaCircular() {
        System.out.println("\nContenido de la cola circular:");

        if (sizeCircular == 0) {
            System.out.println("La cola está vacía.");
        } else {
            System.out.println("Elementos de la cola circular:");

            for (int i = 0; i < sizeCircular; i++) {
                System.out.print(colaCircular[(frenteCircular + i) % capacidadCircular] + " ");
            }
            System.out.println();
        }
    }

    private static void insercionColaCircular(int num) // INSERCIÓN DE CONTENIDO A LA COLA
    {
        System.out.println("\nInserción de contenido en la cola circular:");
        System.out.println("-------------------------------------------");

        if (!colaCircularLlena()) {
            finalColaCircular = (finalColaCircular + 1) % capacidadCircular;
            colaCircular[finalColaCircular] = num;
            sizeCircular++;
            System.out.println("El número " + num + " ha sido insertado en la cola circular.");
            System.out.println("Estado de la cola circular después de la inserción:");
            contenidoColaCircular();
        } else {
            System.out.println("Desbordamiento! No se puede insertar el número. La cola está llena.");
        }
    }

    private static void eliminacionColaCircular() // ELIMINACIÓN DE CONTENIDO EN LA COLA
    {
        System.out.println("\nEliminación de contenido en la cola circular:");
        System.out.println("--------------------------------------------");

        if (!colaCircularVacia()) {
            int numeroEliminado = colaCircular[frenteCircular];
            frenteCircular = (frenteCircular + 1) % capacidadCircular;
            sizeCircular--;
            System.out.println("El número eliminado es: " + numeroEliminado);
            System.out.println("Estado de la cola circular después de la eliminación:");
            contenidoColaCircular();
        } else {
            System.out.println("Subdesbordamiento! No se puede eliminar ningún elemento. La cola está vacía.");
        }
    }

    private static boolean colaCircularVacia() {
        return sizeCircular == 0;
    }

    private static boolean colaCircularLlena() {
        return sizeCircular == capacidadCircular;
    }

    private static int frenteColaCircular() {
        return colaCircular[frenteCircular];
    }

    public static void main(String[] args) {
        Scanner objLeer = new Scanner(System.in);

        // MENÚ DE OPCIONES
        int opcion = 0;
        while (opcion != 3) {
            System.out.println("\nMenú de opciones");
            System.out.println("1. Cola Lineal");
            System.out.println("2. Cola Circular");
            System.out.println("3. Salir");
            System.out.println("Opción: ");
            opcion = objLeer.nextInt();

            switch (opcion) {
                case 1:
                    System.out.println("\nMenú de opciones - Cola Lineal\n");
                    int opcionCL;
                    do {
                        System.out.println("1. Insertar número en la cola");
                        System.out.println("2. Eliminar número de la cola");
                        System.out.println("3. Mostrar contenido de la cola");
                        System.out.println("4. Mostrar número en el frente de la cola");
                        System.out.println("5. Volver");
                        System.out.print("Opción: ");
                        opcionCL = objLeer.nextInt();

                        switch (opcionCL) {
                            case 1:
                                if (!colaLinealLlena()) {
                                    System.out.print("Ingrese número deseado: ");
                                    int num = objLeer.nextInt();
                                    insercionColaLineal(num);
                                    System.out.println("El número " + num + " ha sido insertado en la cola lineal");
                                } else
                                    System.out.println("Desbordamiento! No se puede insertar el número");
                                break;

                            case 2:
                                if (!colaLinealVacia())
                                    eliminacionColaLineal();
                                else
                                    System.out.println("Subdesbordamiento! No se puede eliminar ningún elemento");
                                break;

                            case 3:
                                contenidoColaLineal();
                                break;

                            case 4:
                                if (!colaLinealVacia())
                                    System.out.println("El número en el frente de la cola es: " + frenteColaLineal());
                                else
                                    System.out.println("La cola está vacía");
                                break;

                            case 5:
                                System.out.println("Volviendo al menú principal...");
                                break;

                            default:
                                System.out.println("Opción inválida. Intente nuevamente");
                                break;
                        }
                    } while (opcionCL != 5);
                    break;

                case 2:
                    System.out.println("\nMenú de opciones - Cola Circular\n");
                    int opcionCC;
                    do {
                        System.out.println("1. Insertar número en la cola");
                        System.out.println("2. Eliminar número de la cola");
                        System.out.println("3. Mostrar contenido de la cola");
                        System.out.println("4. Mostrar número en el frente de la cola");
                        System.out.println("5. Mostrar número en el final de la cola");
                        System.out.println("6. Volver");
                        System.out.print("Opción: ");
                        opcionCC = objLeer.nextInt();

                        switch (opcionCC) {
                            case 1:
                                if (!colaCircularLlena()) {
                                    System.out.print("Ingrese número deseado: ");
                                    int num = objLeer.nextInt();
                                    insercionColaCircular(num);
                                    System.out.println("El número " + num + " ha sido insertado en la cola circular");
                                }

                                else
                                    System.out.println("Desbordamiento! No se puede insertar el número");
                                break;

                            case 2:
                                if (!colaCircularVacia())
                                    eliminacionColaCircular();
                                else
                                    System.out.println("Subdesbordamiento! No se puede eliminar ningún elemento");
                                break;

                            case 3:
                                contenidoColaCircular();
                                break;

                            case 4:
                                if (!colaCircularVacia())
                                    System.out.println("El número en el frente de la cola es: " + frenteColaCircular());
                                else
                                    System.out.println("La cola está vacía");
                                break;

                            case 5:
                                if (!colaCircularVacia())
                                    System.out.println(
                                            "El número en el final de la cola es: " + colaCircular[finalColaCircular]);
                                else
                                    System.out.println("La cola está vacía");
                                break;

                            case 6:
                                System.out.println("Volviendo al menú principal...");
                                break;

                            default:
                                System.out.println("Opción inválida. Intente nuevamente");
                                break;
                        }
                    } while (opcionCC != 6);
                    break;

            }
        }
        objLeer.close();
    }
}
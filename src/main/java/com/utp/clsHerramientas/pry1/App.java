package com.utp.clsHerramientas.pry1;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import com.utp.utils.Cli;
import com.utp.utils.Utils;

// · Opción 1: Método llamado mtdListaNumeros, que determina la media de una lista indefinida de números positivos, terminados con un número negativo. La entrada y salidas deben realizarse dentro del método principal.
public class App {
    public static void main(String[] args) {
        Methods methods = new Methods();
        while (true) {
            String entrada = Cli.read_non_empty_input("Ingrese una opción,");
            if (entrada.equals("exit")) {
                System.out.println("Saliendo...");
                System.exit(0);
            }
            Optional<Integer> maybe_option = Utils.try_parse_int(entrada);
            if (maybe_option.isEmpty()) {
                System.out.println("Opción no es un número convertible.");
                continue;
            }
            var option = maybe_option.get();
            System.out.println("Opción seleccionada: " + option);
            switch (option) {
                default -> System.out.println("Opción no válida.");
                case 1 -> {
                    System.out.println("\nIngrese números positivos, para terminar ingrese un número negativo.");
                    ListaDeNumeros lista = new ListaDeNumeros();
                    while (true) {
                        String input = Cli.read_non_empty_input("Ingrese un número,");
                        Optional<Integer> resultado = lista.push_str(input);
                        if (resultado.isEmpty()) {
                            System.out.println("El número no es válido.");
                            continue;
                        }
                        // break if read negative number
                        if (resultado.get() < 0) {
                            break;
                        }
                    }
                    Optional<Double> resultado = methods.mtdListaNumeros(lista);
                    // Una lista vacía fue proveída
                    if (resultado.isEmpty()) {
                        System.out.println("No se ingresaron números.");
                        continue;
                    }
                    System.out.println("La media de los números ingresados es: " + resultado.get());
                }
                case 2 -> {
                    String entrada2 = Cli.read_non_empty_input("Ingrese un número para obtener su factorial,");
                    methods.mtdFactorial(entrada2);
                }
                case 3 -> methods.mtdMediaValor();
                case 4 -> {
                    System.out.println("\nIngrese números, para terminar ingrese un número entre 1 y 5.");
                    while (true) {
                        if (ControlFlow.BREAK == methods.mtdNumeros()) {
                            break;
                        }
                    }
                }
            }
        }

    }
}

class ListaDeNumeros {
    ArrayList<Integer> Lista = new ArrayList<>();

    ArrayList<Integer> getLista() {
        return Lista;
    }

    Optional<Integer> push_str(String input) {
        Optional<Integer> num = Utils.try_parse_int(input);
        if (num.isPresent()) {
            Lista.add(num.get());
        }
        return num;
    }
}

class Methods {
    // • Opción 1: Método llamado mtdListaNumeros, que determina la media de una
    // lista indefinida de números positivos, terminados con un número negativo. La
    // entrada y salidas deben realizarse dentro del método principal.
    public Optional<Double> mtdListaNumeros(ListaDeNumeros lista) {
        if (lista.getLista().isEmpty()) {
            return Optional.empty();
        }
        double sum = 0;
        for (int item : lista.getLista()) {
            sum += item;
        }
        return Optional.of(sum / lista.getLista().size());
    }

    // · Opción 2: Método llamado mtdFactorial que contenga el cálculo del factorial
    // de un número leído en el método principal. La impresión del resultado se
    // realiza en el método mtdFactorial.
    public void mtdFactorial(String string_num) {
        Optional<Integer> maybe_num = Utils.try_parse_int(string_num);
        if (maybe_num.isEmpty()) {
            System.out.println("El número no es válido.");
            return;
        }
        var num = maybe_num.get();
        if (num < 0) {
            System.out.println("El número no puede ser negativo.");
            return;
        }
        BigDecimal factorial = new BigDecimal(1);
        for (int i = 1; i <= num; i++) {
            factorial = factorial.multiply(new BigDecimal(i));
        }
        System.out.println("El factorial de " + num + " es: " + Utils.pretty_number(factorial));
    }

    // • Opción 3: Método llamado mtdMediaValor que dispone de un cierto número de
    // parejas de valores de los cuales el último es el 999 y se desea determinar e
    // imprimir el valor máximo de las medias correspondientes a parejas de valores
    // sucesivos. La entrada y de datos y cálculos se debe realizar en el método
    // mtdMediaValor y la impresión en el método principal.
    public void mtdMediaValor() {

    }

    // • Opción 4: Método llamado mtdNumeros que lea e imprima sucesivamente números
    // desde el teclado hasta que se introduzca un número comprendido entre 1 y 5.
    // La entrada y salidas deben realizarse dentro del método mtdNumeros.
    public ControlFlow mtdNumeros() {
        var user_input = Cli.read_non_empty_input("Ingrese un número, ");
        Optional<Integer> maybe_num = Utils.try_parse_int(user_input);
        if (maybe_num.isEmpty()) {
            System.out.printf("\"%s\" no es número no es válido.\n", user_input);
            return ControlFlow.CONTINUE;
        }
        var num = maybe_num.get();
        if (num >= 1 && num <= 5) {
            System.out.printf("\"%d\" SI está comprendido entre 1 y 5.\n", num);
            return ControlFlow.BREAK;
        }
        System.out.printf("\"%d\" NO está comprendido entre 1 y 5.\n", num);
        return ControlFlow.CONTINUE;
    }
}

enum ControlFlow {
    CONTINUE, BREAK
}
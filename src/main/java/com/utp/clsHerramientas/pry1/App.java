package com.utp.clsHerramientas.pry1;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import com.utp.utils.Utils;
import com.utp.utils.Cli;

// · Opción 1: Método llamado mtdListaNumeros, que determina la media de una lista indefinida de números positivos, terminados con un número negativo. La entrada y salidas deben realizarse dentro del método principal.
public class App {
    public static void main(String[] args) {
        Methods methods = new Methods();
        while (true) {
            String entrada = Cli.read_non_empty_input(
                    "Ingrese una opción (1: mtdListaNumeros, 2: mtdFactorial, 3: mtdMediaValor, 4: mtdNumeros),\n");
            Optional<Integer> maybe_option = Utils.try_parse_int(entrada);
            if (maybe_option.isEmpty()) {
                System.out.println("Opción no es un número convertible.");
                continue;
            }
            var option = maybe_option.get();
            System.out.println("Opción seleccionada: " + option);
            switch (option) {
                case 1 -> { // mtdListaNumeros
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
                case 2 -> { // mtdFactorial
                    String entrada2 = Cli.read_non_empty_input("Ingrese un número para obtener su factorial,");
                    methods.mtdFactorial(entrada2);
                }
                case 3 -> methods.mtdMediaValor(); // mtdMediaValor
                case 4 -> { // mtdNumeros
                    System.out.println("\nIngrese números, para terminar ingrese un número entre 1 y 5.");
                    while (true) {
                        if (ControlFlow.BREAK == methods.mtdNumeros()) {
                            break;
                        }
                    }
                }
                default -> System.out.println("Opción no válida.");
            }
        }

    }
}

class ListaDeNumeros {
    ArrayList<Integer> lista = new ArrayList<>();

    ArrayList<Integer> getLista() {
        return lista;
    }

    Optional<Integer> push_str(String input) {
        Optional<Integer> num = Utils.try_parse_int(input);
        if (num.isPresent()) {
            lista.add(num.get());
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
        Optional<BigDecimal> media_mayor = Optional.empty();
        Integer[] pareja = new Integer[2];
        ciclo_parejas: while (true) {
            String[] str_pareja = Cli
                    .read_non_empty_input(
                            "Ingrese dos numeros separados con un coma. Ingrese 999 para proseguir a imprimir el resultado.")
                    .split(",", 2);
            if (str_pareja.length != 2) {
                System.out.println("Debe ingresar dos números separados por coma.");
                continue;
            }
            for (int i = 0; i < 2; i++) {
                var entry = str_pareja[i].trim();
                Optional<Integer> maybe_num = Utils.try_parse_int(entry);
                if (maybe_num.isEmpty()) {
                    System.out.println("`" + entry + "` No es convertible a número.");
                    continue ciclo_parejas;
                }
                if (maybe_num.get() == 999) {
                    break ciclo_parejas;
                }
                pareja[i] = maybe_num.get();
            }
            var insertable = BigDecimal.valueOf((pareja[0] + pareja[1]) / 2.0);
            if (media_mayor.isEmpty()) {
                media_mayor = Optional.of(insertable);
            } else {
                var media_actual = media_mayor.get();
                if (insertable.compareTo(media_actual) > 0) {
                    media_mayor = Optional.of(insertable);
                }
            }
        }
        if (media_mayor.isEmpty()) {
            System.out.println("No se ingresaron parejas de números.");
        } else {
            System.out.println("La media más alta es: " + media_mayor.get());
        }
    }

    // • Opción 4: Método llamado mtdNumeros que lea e imprima sucesivamente números
    // desde el teclado hasta que se introduzca un número comprendido entre 1 y 5.
    // La entrada y salidas deben realizarse dentro del método mtdNumeros.
    public ControlFlow mtdNumeros() {
        var user_input = Cli.read_non_empty_input("Ingrese un número, ");
        Optional<Integer> maybe_num = Utils.try_parse_int(user_input);
        if (maybe_num.isEmpty()) {
            System.out.printf("\"%s\" no es número no es válido.%n", user_input);
            return ControlFlow.CONTINUE;
        }
        var num = maybe_num.get();
        if (num >= 1 && num <= 5) {
            System.out.printf("\"%d\" SI está comprendido entre 1 y 5.%n", num);
            return ControlFlow.BREAK;
        }
        System.out.printf("\"%d\" NO está comprendido entre 1 y 5.%n", num);
        return ControlFlow.CONTINUE;
    }
}

enum ControlFlow {
    CONTINUE, BREAK
}
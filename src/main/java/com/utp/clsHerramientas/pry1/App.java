package com.utp.clsHerramientas.pry1;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import com.utp.utils.Utils;

// · Opción 1: Método llamado mtdListaNumeros, que determina la media de una lista indefinida de números positivos, terminados con un número negativo. La entrada y salidas deben realizarse dentro del método principal.
public class App {
    public static void main(String[] args) {
        ListaDeNumeros lista = new ListaDeNumeros();
        Methods methods = new Methods();
        System.out.println("La media de los números ingresados es: " + methods.mtdListaNumeros(lista));

        for (int num = 80; num < 180; num++) {

            methods.mtdFactorial(String.format("%d", num));
        }
    }
}

class ListaDeNumeros {
    ArrayList<Integer> Lista = new ArrayList<>();

    ArrayList<Integer> getLista() {
        return Lista;
    }

    Optional<Integer> push_str(String input) {
        Optional<Integer> num = Optional.ofNullable(Integer.parseInt(input));
        if (num.isPresent()) {
            Lista.add(num.get());
        }
        return num;
    }
}

class Methods {
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
        var maybe_num = Optional.ofNullable(Integer.parseInt(string_num));
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

}
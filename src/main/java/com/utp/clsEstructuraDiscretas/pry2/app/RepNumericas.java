package com.utp.clsEstructuraDiscretas.pry2.app;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.Optional;

import com.utp.utils.Result;

public class RepNumericas {
    static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    static String buffer = null;

    static String ReadLine() {
        try {
            buffer = reader.readLine().trim();
        } catch (Exception e) {
            System.out.println("Entrada invalida: " + e);
        }

        if (buffer.equals("exit")) {
            System.out.println("Saliendo...");
            System.exit(0);
        }

        return buffer;
    }

    public static void main(String[] args) {
        String entrada = null;
        System.out.println("Bienvenido al programa de representación numérica. Escriba `exit` para salir cuando sea.");
        while (true) {

            System.out.println("Elija la representación numérica (binario, entero, hexadecimal): ");
            // El programa debe aceptar un número ingresado por el usuario, indicando si es
            // binario, decimal o hexadecimal.
            entrada = ReadLine();
            Optional<RepNumerica> maybe_rep = RepNumerica.from_str(entrada);
            if (maybe_rep.isEmpty()) {
                System.out.printf("`%s` No es una representación válida.\n", entrada);
                continue;
            }
            // Guardamos la representación numérica
            var representacion = maybe_rep.get();
            System.out.printf("Elegiste la representación `%s`\n", representacion);
            System.out.println("Ingrese el número a representar: ");
            // Leemos el número a representar en la representación seleccionada
            entrada = ReadLine();
            Result<BigInteger, String> maybe_BigInt = RepresentacionNumerica.check_string(representacion, entrada);
            if (maybe_BigInt.isError()) {
                System.out.println(maybe_BigInt.unwrapError());
                continue;
            }
            // Guardamos el número.
            var bigInt = maybe_BigInt.unwrapOk();
            // Creamos una instancia de RepresentacionNumerica que puede ser convertida a
            // cualquier otra representación soportada
            RepresentacionNumerica rep = new RepresentacionNumerica(bigInt, representacion);
            System.out.println("El número en la representación seleccionada es: " + rep.to_string());

            // Mostrar en pantalla los equivalentes del número ingresado en los sistemas
            // numéricos restantes, según corresponda
            switch (representacion) {
                case BINARIO -> {
                    System.out.println("El número en entero es: " + rep.as_string_in(RepNumerica.ENTERO));
                    System.out.println("El número en hexadecimal es: " + rep.as_string_in(RepNumerica.HEXADECIMAL));
                }
                case ENTERO -> {
                    System.out.println("El número en binario es: " + rep.as_string_in(RepNumerica.BINARIO));
                    System.out.println("El número en hexadecimal es: " + rep.as_string_in(RepNumerica.HEXADECIMAL));
                }
                case HEXADECIMAL -> {
                    System.out.println("El número en binario es: " + rep.as_string_in(RepNumerica.BINARIO));
                    System.out.println("El número en entero es: " + rep.as_string_in(RepNumerica.ENTERO));
                }
            }
        }
    }
}

record RepresentacionNumerica(BigInteger innerBigInt, RepNumerica type) {

    public static Result<RepresentacionNumerica, String> from_parts(String innerString, String type) {
        Optional<RepNumerica> maybe_rep = RepNumerica.from_str(type);
        if (maybe_rep.isEmpty()) {
            return Result.error(String.format("`%s` is not a valid representation type", type));
        }
        var representacion = maybe_rep.get();
        Result<BigInteger, String> maybe_BigInt = check_string(representacion, innerString);
        if (maybe_BigInt.isError()) {
            return Result.error(maybe_BigInt.unwrapError());
        }
        return Result.ok(new RepresentacionNumerica(maybe_BigInt.unwrapOk(), representacion));
    }

    // Converts, if possible, the string to a BigInteger representation of it.
    // otherwise it returns an error message
    public static Result<BigInteger, String> check_string(RepNumerica type, String innerString) {
        // Trims leading zeroes because they don't affect the return value
        // numeric representation
        innerString = innerString.replaceFirst("^[0]*", "").trim().toUpperCase();
        int radix = 0;
        switch (type) {
            case BINARIO -> {
                if (!innerString.matches("[01]+")) {
                    return Result.error("Binario solo puede contener 0s y 1s");
                }
                radix = 2;
            }
            case ENTERO -> {
                if (!innerString.matches("[0-9]+")) {
                    return Result.error("Entero solo puede contener dígitos");
                }
                radix = 10;
            }
            case HEXADECIMAL -> {
                if (!innerString.matches("[0-9A-F]+")) {
                    return Result.error("Hexadecimal solo puede contener dígitos y letras de la A a la F");
                }
                radix = 16;
            }
        }
        return Result.ok(new BigInteger(innerString, radix));
    }

    public String to_string() {
        return as_string_in(type);
    }

    public String as_string_in(RepNumerica type) {
        // Convertir el número ingresado a sus equivalentes en un sistema numérico.
        return switch (type) {
            case BINARIO -> "0b" + innerBigInt.toString(2);
            case ENTERO -> innerBigInt.toString(10);
            case HEXADECIMAL -> "0x" + innerBigInt.toString(16).toUpperCase();
        };
    }

}

enum RepNumerica {
    BINARIO, ENTERO, HEXADECIMAL;

    public static Optional<RepNumerica> from_str(String input) {
        input = input.trim().toLowerCase();
        switch (input) {
            case "binario":
                return Optional.of(BINARIO);
            case "entero":
                return Optional.of(ENTERO);
            case "hexadecimal":
                return Optional.of(HEXADECIMAL);
            default:
                return Optional.empty();
        }
    }
}

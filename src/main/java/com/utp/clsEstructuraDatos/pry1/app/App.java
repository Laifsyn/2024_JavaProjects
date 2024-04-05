package com.utp.clsEstructuraDatos.pry1.app;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class App {
    void spawn_cli() {
        // Enter data using BufferReader
        var reader = new BufferedReader(new InputStreamReader(System.in));
        String input;
        while (true) {

            // Reading data using readLine
            try {
                System.out.print("Ingrese numero o `exit` para salir: ");
                input = reader.readLine().trim();
            } catch (Exception e) {
                System.out.println("Entrada invalida: " + e);
                continue;
            }
            if (input.equals("exit")) {
                System.out.println("Saliendo...");
                break;
            }
            // Sanitize input
            if (input.isBlank()) {
                System.out.println("Número inválido `" + input + "` no puede estar vacío");
                continue;
            }
            if (input.contains(".")) {
                System.out.println("Número inválido `" + input + "` no puede contener decimales");
                continue;
            }
            if (input.startsWith("0") || input.startsWith("-")) {
                System.out.println("Número inválido `" + input + "` no puede empezar con `0` o `-`");
                continue;
            }
            int num = 0;
            try {
                num = Integer.parseInt(input);
            } catch (Exception e) {
                System.out.println("Número inválido `" + input + "` no es convertible a un número entero");
                continue;
            }
            if (!(num < 1_000_000_000)) {
                System.out.println("Número inválido `" + input + "` no puede ser mayor a 1_000_000_000");
                continue;
            }
            // Process input after sanitization
            System.out.print("Entrada: ");
            Utils.pretty_print_int(num);
            System.out.println(" => " + Int2Words.to_cardinal(num));
        }
    }

    public static void main(String[] args) {
        System.out.println("Hello World!");
        App app = new App();
        app.spawn_cli();
    }
}

class Int2Words {

    static String to_cardinal(Integer num) {
        if (num == 0) {
            return "cero";
        }
        var triplets = Utils.into_triplets(num);

        var words = new ArrayList<String>();
        for (int i = triplets.length - 1; i >= 0; i--) {
            // Read the triplets in an inverted order
            var triplet = triplets[triplets.length - i - 1];
            if (triplet == 0) {
                continue;
            }
            int hundreds = (triplet / 100) % 10;
            int tens = (triplet / 10) % 10;
            int units = triplet % 10;
            if (hundreds > 0) {
                if (triplet == 100) {
                    // Edge case when triplet is a hundred
                    words.add("cien");
                } else {
                    words.add(CENTIS[hundreds]);
                }
            }

            if (tens != 0 || units != 0) {
                // for edge case when unit value is 1 and is not the last triplet
                String unit_word = null;
                if (units == 1 && i != 0) {
                    unit_word = "un";
                } else {
                    unit_word = UNIDADES[units];
                }
                if (tens == 0) {
                    // case `?_102` => `? ciento dos`
                    words.add(unit_word);
                } else if (tens == 1) {
                    // case `?_119` => `? ciento diecinueve`
                    // case `?_110` => `? ciento diez`
                    words.add(DIECIS[units]);
                } else {
                    // case `?_142 => `? cuarenta y dos`
                    String ten = DECENAS[tens];
                    if (units == 0) {
                        words.add(ten);
                    } else {
                        words.add(String.format("%s y %s", ten, unit_word));
                    }
                }
                if (i != 0) {
                    if (i > MILES.length - 1) {
                        return String.format("Número demasiado grande: %d - Maximo: %d", num, Integer.MAX_VALUE);
                    }
                    // Boolean that checks if next MILES is plural
                    boolean plural = triplet != 1;
                    if (plural) {
                        words.add(MILES[i]);
                    } else {
                        words.add(MIL[i]);
                    }
                }

            }
        }
        return String.join(" ", words);
    }

    static final String[] UNIDADES = {"", "uno", "dos", "tres", "cuatro", "cinco", "seis", "siete", "ocho", "nueve"};
    static final String[] DIECIS = {"diez", "once", "doce", "trece", "catorce", "quince", "dieciséis", "diecisiete",
            "dieciocho", "diecinueve"};
    static final String[] DECENAS = {"", "", "veinte", "treinta", "cuarenta", "cincuenta", "sesenta", "setenta",
            "ochenta", "noventa"};
    static final String[] CENTIS = {"", "ciento", "doscientos", "trescientos", "cuatrocientos", "quinientos",
            "seiscientos",
            "setecientos", "ochocientos", "novecientos"};
    static final String[] MILES = {"", "mil", "millones", "billones"};
    static final String[] MIL = {"", "mil", "millón", "billón"};
}

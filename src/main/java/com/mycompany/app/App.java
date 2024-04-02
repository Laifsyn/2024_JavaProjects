/*----------------------------------------------------------------------------------------
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for license information.
 *---------------------------------------------------------------------------------------*/

package com.mycompany.app;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

public class App {
    void spawn_cli() {
        // Enter data using BufferReader
        var reader = new BufferedReader(new InputStreamReader(System.in));
        String input;
        while (true) {


            // Reading data using readLine
            try {
                System.out.println("Ingrese numero o `exit` para salir:");
                input = reader.readLine().trim();
            } catch (Exception e) {
                System.out.println("Entrada invalida: " + e);
                continue;
            }
            if (input.equals("exit")) {
                System.out.println("Saliendo...");
                break;
            }
            if (input.equals("clear")) {
                System.out.println("\033[H\033[2J");
                continue;
            }
        }

        // loop {
        
        // match input {
        // "exit" => {
        // clear_terminal();
        // println!("Saliendo...");
        // break;
        // }
        // "clear" => {
        // clear_terminal();
        // continue;
        // }
        // _ => {}
        // }
        // if input.is_empty() {
        // println!("Número inválido {input:?} no puede estar vacío");
        // continue;
        // }
        // if input.starts_with(['0', '-']) && input.len() > 1 {
        // println!("Número inválido {input:?} no puede empezar con `0` o `-`");
        // continue;
        // }
        // let num = match input.parse::<i128>() {
        // Ok(num) => num,
        // Err(_) => {
        // println!("Número inválido {input:?} - no es convertible a un número entero");
        // continue;
        // }
        // };
        // if let true = num >= 1_000_000_000 {
        // println!("Número inválido {input:?} - Tiene que ser menor que
        // 1_000_000_000");
        // continue;
        // }
        // print!("Entrada:");
        // pretty_print_int(num);
        // println!(" => {:?}", es.to_cardinal(num).unwrap());
        // }

    }

    public static void main(String[] args) {
        System.out.println("Hello World!");
        App app = new App();

        app.spawn_cli();
        var palabra = Int2Words.to_cardinal(1_001_020_642);
        System.out.println("Goodbye World!: " + palabra);
        System.out.println("=" + Int2Words.to_cardinal(2_001_020_642));
        System.out.println("=" + Int2Words.to_cardinal(2_002_020_642));
        System.out.println("=" + Int2Words.to_cardinal(2_020_021_642));
        System.out.println("=" + Int2Words.to_cardinal(2_000_020_642));
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
                if (i != 0 && triplet != 0) {
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

    static final String[] UNIDADES = { "", "uno", "dos", "tres", "cuatro", "cinco", "seis", "siete", "ocho", "nueve" };
    static final String[] DIECIS = { "diez", "once", "doce", "trece", "catorce", "quince", "dieciséis", "diecisiete",
            "dieciocho", "diecinueve" };
    static final String[] DECENAS = { "", "", "veinte", "treinta", "cuarenta", "cincuenta", "sesenta", "setenta",
            "ochenta", "noventa" };
    static final String[] CENTIS = { "", "ciento", "doscientos", "trescientos", "cuatrocientos", "quinientos",
            "seiscientos",
            "setecientos", "ochocientos", "novecientos" };
    static final String[] MILES = { "", "mil", "millones", "billones" };
    static final String[] MIL = { "", "mil", "millón", "billón" };
}

class Utils {
    static public Integer[] into_triplets(Integer num) {

        var vec = new ArrayList<Integer>();
        while (num > 0) {
            vec.add(num % 1000);
            num /= 1000;
        }
        Collections.reverse(vec);
        // System.out.println(vec.toString());
        return vec.toArray(new Integer[0]);
    }

    static public void pretty_print_int(Integer num) {
        var triplets = Utils.into_triplets(num);
        for (int i = 0; i < triplets.length; i++) {
            System.out.print(triplets[i]);
            if (i < triplets.length - 1) {
                System.out.print(",");
            }
        }
    }
}
package com.utp.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Cli {
    static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public static String read_non_empty_input(String msg) {
        String input;
        // Reading data using readLine
        while (true) {
            try {
                System.out.printf("%s o `exit` para salir: ", msg);
                input = reader.readLine().trim();
            } catch (Exception e) {
                System.out.println("Entrada invalida: " + e);
                continue;
            }
            if (input.equals("exit")) {
                System.out.println("Saliendo...");
                System.exit(0);
            }
            if (input.isBlank()){
                System.out.println("Entrada no puede estar vacía");
                continue;
            }
            break;
        }

        return input;
    }
}

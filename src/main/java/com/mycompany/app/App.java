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

        // Reading data using readLine
        String name = new String("asdasd");
        try {
            System.out.println("Ingrese info");
            name = reader.readLine();
            System.out.println("FIn");
        } catch (Exception e) {
            System.out.println("Errir");
            System.out.println(e);
        }
        

        System.out.println("FIn2");
        // Printing the read line
        System.out.println(name);
        // let mut input = String::new();
        // print!("\nIngrese un número para convertir a palabras\nIngrese `exit` para
        // salir:\n\n");
        // fn read_line(input: &mut String) {
        // input.clear();
        // std::io::stdin().read_line(input).unwrap();
        // }

        // loop {
        // print!("Ingrese su número: ");
        // flush();
        // read_line(&mut input);
        // let input = input.trim();
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
        System.out.println("Goodbye World!");
    }
}

class Int2Words {

    // let mut vec = vec![];
    // while num > 0 {
    // vec.push((num % 1000) as i16);
    // num /= 1000;
    // }
    // vec.reverse();
    // let prettied =
    // vec.into_iter().map(|num|
    // format!("{num:03}")).collect::<Vec<String>>().join(",");

    // print!("{:?}", prettied.trim_start_matches('0'));
    // flush();

}

class Utils {
    static Integer[] into_triplets(Integer num) {

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
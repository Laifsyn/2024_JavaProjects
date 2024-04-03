package com.utp.pry2.app;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Optional;

import com.utp.Cli;
import com.utp.Traits.FromStr;

import io.vavr.control.Validation;

public class RepNumericas {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        System.out.println("RepNumericas");
        (new RepNumericas()).spawn_cli();
    }

    void spawn_cli() {
        // Enter data using BufferReader
        var reader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            // var input = Cli.read_non_empty_input("Ingrese un binario");
            var input = "0010101010101000101";
            var maybe_binario = BinaryString.from(input);
            if (maybe_binario.isInvalid()) {
                System.out.println(maybe_binario.getError());
                continue;
            }
            
            System.out.printf("Entrada: %s\n", input);
            System.out.println("Hexadecimal: 0x" + maybe_binario.get().to_hex_string());
            var decimal = maybe_binario.get().to_int();
            System.out.printf("%s => Entero: ", input );
            Utils.pretty_print_int(decimal);
            System.out.println("");
            break;
        }
    }
}

class Utils {
    static void pretty_print_int(int num) {
        com.utp.pry1.app.Utils.pretty_print_int(num);

    }
}

class BinaryString implements FromStr<BinaryString> {
    public static Validation<String, BinaryString> from(String input){
        return (new BinaryString(null)).from_str(input);
    }
    public Validation<String, BinaryString> from_str(String input) {
        if (input.length() > BITS_SIZE) {
            return Validation.invalid(String.format("`%s` is longer than the set character limit", input));
        }
        if (!input.matches("[01]+")) {
            return Validation.invalid(String.format("`%s` is not a valid binary string", input));
        }
        return Validation.valid(new BinaryString(input));
    }

    public Integer to_int() {
        if (cached_int.isPresent()) {
            return cached_int.get();
        }
        return Integer.parseInt(inner, 2);
    }

    public String to_hex_string() {
        return Integer.toHexString(to_int());
    }

    BinaryString(String inner) {
        this.inner = inner;
    }

    final static int BITS_SIZE = 31;
    String inner = null;
    Optional<Integer> cached_int = Optional.empty();
}
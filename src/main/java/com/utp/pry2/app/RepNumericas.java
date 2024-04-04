package com.utp.pry2.app;

import java.util.Optional;

import com.utp.io_vavrs.Validation;
import com.utp.utils.Cli;

public class RepNumericas {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        System.out.println("Representación Numéricas - Binario a Hexadecimal y Decimal\n");
        (new RepNumericas()).spawn_cli();
    }

    void spawn_cli() {
        // Enter data using BufferReader
        while (true) {
            var input = Cli.read_non_empty_input("Ingrese un binario");
            var maybe_binario = BinaryString.from_str(input);
            if (maybe_binario.isInvalid()) {
                System.out.println(maybe_binario.getError());
                continue;
            }
            input = maybe_binario.get().toString();
            System.out.printf("Entrada: %s\n", input);
            System.out.println("Hexadecimal: 0x" + maybe_binario.get().to_hex_string());
            System.out.printf("%s => Entero: ", input);
            Utils.pretty_print_int(maybe_binario.get().to_decimal());
            System.out.println("\n");
        }
    }
}

class Utils {
    static void pretty_print_int(int num) {
        com.utp.pry1.app.Utils.pretty_print_int(num);
    }
}

class BinaryString {
    public static Validation<String, BinaryString> from_str(String input) {
        // Strip leading zeroes because they don't affect the return value size
        input = input.replaceFirst("^[0]*", "");
        if (input.length() > BITS_SIZE) {
            return Validation.invalid(
                    String.format("`%s` is `%d` chars longer than the set character limit of `%d` chars", input,
                            input.length() - BITS_SIZE, BITS_SIZE));
        }
        if (!input.matches("[01]+")) {
            return Validation.invalid(String.format("`%s` is not a valid binary string", input));
        }
        return Validation.valid(new BinaryString(input));
    }

    public Integer to_decimal() {
        if (cached_int.isPresent()) {
            return cached_int.get();
        }
        cached_int = Optional.of(Integer.parseInt(inner, 2));
        return cached_int.get();
    }

    public String to_hex_string() {
        return Integer.toHexString(to_decimal());
    }

    @Override
    public String toString() {
        return inner;
    }

    private BinaryString(String inner) {
        this.inner = inner;
    }

    final static int BITS_SIZE = 31;
    String inner = null;
    Optional<Integer> cached_int = Optional.empty();
}
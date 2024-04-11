package com.utp.clsEstructuraDatos.pry2.app;

import java.math.BigInteger;
import java.util.Optional;

import com.utp.utils.Result;

public class RepNumericas {

    public static void main(String[] args) {
        BufferedRead
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
        return switch (type) {
            case BINARIO -> innerBigInt.toString(2);
            case ENTERO -> innerBigInt.toString(10);
            case HEXADECIMAL -> innerBigInt.toString(16);
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

package com.utp.clsEstructuraDatos.pry3.Tokens;

import java.util.Optional;

import com.utp.utils.Result;

public class TokenStream {
    final String inner_string;
    int pointer = 0;

    private TokenStream(String string) {
        this.inner_string = string;
    }

    public static Result<TokenStream, IllegalArgumentException> from_string(String string) {
        string = string.strip();
        if (string.length() > Integer.MAX_VALUE) {
            return Result.error(new IllegalArgumentException(
                    "string too long. Keep it under " + Integer.MAX_VALUE + " characters"));
        }
        return Result.ok(new TokenStream(string));
    }

    int next_offset = 1;

    Optional<Token> next() {
        if (pointer >= inner_string.length()) {
            return Optional.empty();
        }
        // Avanza el puntero por cada espacio en blanco
        while (pointer < inner_string.length() && Character.isWhitespace(inner_string.charAt(pointer))) {
            pointer++;
        }
        Token result = Token.fromString(inner_string.substring(pointer, pointer + next_offset));
        // Use Arrow syntax
        switch (result) {
            case Token.EMPTY() -> {
                next_offset = 1;
                pointer += next_offset;
                return next();
            }
            case Token.AND(String substring) -> {
                next_offset = 1;
                pointer += substring.length();
                return Optional.of(result);
            }
            case Token.OR(String substring) -> {
                next_offset = 1;
                pointer += substring.length();
                return Optional.of(result);
            }
            case Token.NOT(String substring) -> {
                next_offset = 1;
                pointer += substring.length();
                return Optional.of(result);
            }
            case Token.IMPLICATES(String substring) -> {
                next_offset = 1;
                pointer += substring.length();
                return Optional.of(result);
            }
            case Token.XAND(String substring) -> {
                next_offset = 1;
                pointer += substring.length();
                return Optional.of(result);
            }
            case Token.CLOSE_PAREN(char _open_paren) -> {
                next_offset = 1;
                pointer += next_offset;
                return Optional.of(result);
            }
            case Token.OPEN_PAREN(char _close_paren) -> {
                next_offset = 1;
                pointer += next_offset;
                return Optional.of(result);
            }
            case Token.IDENTIFIER(String substring) -> {
                next_offset = 1;
                pointer += substring.length();
                return Optional.of(result);
            }
            case Token.EXPECTS(String stored_substring, Optional<String> might_expect) -> {
                if (might_expect.isEmpty()) {
                    // Hay match, pero se desconoce qué sigue. Así que aumentamos el offset, y
                    // continuamos.
                    next_offset++;
                    // pero si el offset se sale de rango, significa que no hay match.
                    if (pointer + next_offset >= inner_string.length()) {
                        return Optional.of(new Token.EOL(inner_string.substring(pointer)));
                    }
                    return next();
                }
                String expects_next = might_expect.get();
                final int offset = stored_substring.length() + expects_next.length();
                final String next_substring = inner_string.substring(pointer, pointer + offset);
                final String expects_string = stored_substring + expects_next;
                if (!next_substring.equals(expects_string)) {
                    return Optional.of(new Token.UNEXPECTED(next_substring));
                }
                next_offset = offset;
                return next();
            }
            case Token.UNEXPECTED(String substring) -> {
                next_offset = 1;
                // don't advance the pointer
                return Optional.of(result);
            }
            case Token.EOL(String substring) -> {
                next_offset = 1;
                pointer += substring.length();
                return Optional.of(result);
            }
            // default -> throw new Exception("sadasd");
        }

        throw new UnsupportedOperationException("Not supported yet.");

    }

}

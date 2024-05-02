package com.utp.clsEstructuraDatos.pry3.Tokens;

import java.util.HashMap;
import java.util.Optional;

import com.utp.clsEstructuraDatos.pry3.Operator;
import com.utp.utils.Result;

/**
 * Enumeraciones que contienen datos.
 * Basado en esta implementación en reddit por `NitronHX`.
 * 
 * @see <a href=
 *      "https://www.reddit.com/r/java/comments/135i37c/rust_like_enums_in_java/">{@literal
 *      Reddit Post on Result< T >}</a>
 */
public sealed interface Token {
    public final static int MAX_CHARS = TokensGroup.MAX_CHARS;
    public static final HashMap<String, Operator> ops_substrings = TokensGroup.ops_map;

    /**
     * Convierte una subcadena en un token. Si se puede determinar el token, devuelve el token.
     * <p>En caso de no poder, pero se tiene una conjetura de qué es, devuelve Token.EXPECTS({@code substring},?)</p>
     * <p>En caso de poder determinar que no es un token reconocido, devuelve Token.UNEXPECTED({@code substring})</p>
     * 
     * @param substring
     * @return
     */
    public static Token fromString(String substring) {

        if (substring.isEmpty()) {
            return new Token.EMPTY();
        }

        // Revisamos si la subcadena es un token conocido
        Token maybe_token = TokensGroup.tokens_map.get(substring);
        if (maybe_token != null) {
            return maybe_token;
        }
        // Significa que la subcadena "aún" no es conocido, asi que hacemos más
        // chequeos.

        // Revisamos si la subcadena es parte de algun token multi-caracter definido
        Result<Optional<String>, Integer> result = TokensGroup.string_from_substring(substring);
        if (result.isError()) {
            // Si hay más de una coincidencia, entonces no se puede determinar qué token es.
            return new Token.EXPECTS(substring, Optional.empty());
        }
        Optional<String> subcadena = result.unwrapOk();
        if (subcadena.isPresent()) {
            return Token.fromString(subcadena.get());
        } else {
            // Si no hay coincidencias, entonces la subcadena no es un token conocido.
            return new Token.UNEXPECTED(substring);
        }
    }

    public String toString();

    record AND(String inner_string) implements Token {
        @Override
        public String toString() {
            return "&";
        }

    }

    record OR(String inner_string) implements Token {
        @Override
        public String toString() {
            return "|";
        }
    }

    record NOT(String inner_string) implements Token {
        @Override
        public String toString() {
            return "~";
        }
    }

    record IMPLICATES(String inner_string) implements Token {
        @Override
        public String toString() {
            return "->";
        }
    }

    record XAND(String inner_string) implements Token {
        @Override
        public String toString() {
            return inner_string;
        }
    }

    record IDENTIFIER(String ident) implements Token {
        @Override
        public String toString() {
            return "IDENTIFIER(" + ident + ")";
        }
    }

    record OPEN_PAREN(char paren) implements Token {
        @Override
        public String toString() {
            return paren + "";
        }
    }

    record CLOSE_PAREN(char paren) implements Token {
        @Override
        public String toString() {
            return paren + "";
        }
    }

    /**
     * Significa que este token no es reconocido.
     */
    record UNEXPECTED(String substring) implements Token {
        @Override
        public String toString() {
            return "`" + substring + "` is an invalid token";
        }
    }

    record EOL(String inner_string) implements Token {
        @Override
        public String toString() {
            return inner_string;
        }
    }

    /**
     * Un token que espera contiene un substring, y un posible hint hint de qué
     * cadena sigue. Si el hint es vacío, significa que aún no se sabe qué sigue.
     * 
     * @param inner_string
     * @param expects_next
     */
    record EXPECTS(String inner_string, Optional<String> expects_next) implements Token {
        @Override
        public String toString() {
            return "`" + inner_string + "` expects: `" + expects_next.orElseGet(() -> "{unknown}") + "`";
        }
    }

    record EMPTY() implements Token {
        @Override
        public String toString() {
            return "{EMPTY}";
        }
    }

    record BLANKSPACE(int length) implements Token {
        @Override
        public String toString() {
            return " ".repeat(length);
        }
    }

}

// public enum Token {
// AND, OR, NOT, IMPLICATES, XAND, PARENTHESE_OPEN, PARENTHESE_CLOSE,
// IDENTIFIER;
// }

class TokensGroup {
    static final int MAX_CHARS = 3;
    static final String[] IDENT = { "p", "q", "r" };
    static final String[] AND = { "&", "^" };
    static final String[] OR = { "|" };
    static final String[] NOT = { "~", "¬" };
    static final String[] IMPLICATES = { "->" };
    static final String[] XAND = { "<->" };
    static final String[][] DELIMITER = { { "(", ")" } };

    static final HashMap<String, Token> tokens_map = new HashMap<String, Token>() {
        {
            System.out.println(IDENT.toString());
            put(IDENT[0], new Token.IDENTIFIER(IDENT[0]));
            put(IDENT[1], new Token.IDENTIFIER(IDENT[1]));
            put(IDENT[2], new Token.IDENTIFIER(IDENT[2]));
            put(AND[0], new Token.AND(AND[0]));
            put(AND[1], new Token.AND(AND[1]));
            put(OR[0], new Token.OR(OR[0]));
            put(NOT[0], new Token.NOT(NOT[0]));
            put(NOT[1], new Token.NOT(NOT[1]));
            put(IMPLICATES[0], new Token.IMPLICATES(IMPLICATES[0]));
            put(XAND[0], new Token.XAND(XAND[0]));
            put(DELIMITER[0][0], new Token.OPEN_PAREN(DELIMITER[0][0].charAt(0)));
            put(DELIMITER[0][1], new Token.CLOSE_PAREN(DELIMITER[0][1].charAt(0)));

            for (var delimiters : DELIMITER) {
                assert delimiters.length == 2 : delimiters.toString() + " isn't a valid delimiter - Expects 2 elements";
            }

            int static_members = count_ops_members();
            int hashmap_size = size();
            assert hashmap_size == static_members : "TokensGroup size mismatches: " + hashmap_size + " != "
                    + static_members;
        }
    };
    static final HashMap<String, Operator> ops_map = new HashMap<>() {
        {
            put(AND[0], Operator.AND);
            put(AND[1], Operator.AND);
            put(OR[0], Operator.OR);
            put(NOT[0], Operator.NOT);
            put(NOT[1], Operator.NOT);
            put(IMPLICATES[0], Operator.IMPLICATES);
            put(XAND[0], Operator.XAND);
            int static_members = count_ops_members();
            int hashmap_size = size();
            assert hashmap_size == static_members : "TokensGroup size mismatches: " + hashmap_size + " != "
                    + static_members;
        }
    };

    /**
     * Revisa si la subcadena es parte de algún token multi-caracter definido.
     * 
     * @param substring
     * @return Devuelve la cadena que empieza con {@code substring}, i.e.
     *         {@code "hola".startsWith("ho") -> "hola"}
     *         <ul>
     *         <li>{@code Result.error(n)} si hay más de `una` coincidencia</li>
     *         <li>{@code Result.ok(Optional.empty())} si no hay coincidencias</li>
     *         <li>{@code Result.ok(Optional.of(s))} si hay una coincidencia</li>
     *         </ul>
     */
    static Result<Optional<String>, Integer> string_from_substring(final String substring) {

        java.util.ArrayList<String> matches = new java.util.ArrayList<>();
        if (IMPLICATES[0].startsWith(substring))
            matches.add(IMPLICATES[0]);
        if (XAND[0].startsWith(substring)) {
            matches.add(XAND[0]);
        }
        switch (matches.size()) {
            case 0 -> {
                return Result.ok(Optional.empty());
            }
            case 1 -> {
                return Result.ok(Optional.of(matches.get(0)));
            }
            default -> {
                return Result.error(matches.size());
            }
        }
    }

    static int count_ops_members() {
        return AND.length + OR.length + NOT.length + IMPLICATES.length + XAND.length + IDENT.length /*
                                                                                                     * +
                                                                                                     * DELIMITER.length
                                                                                                     * * 2
                                                                                                     */;
    }
}

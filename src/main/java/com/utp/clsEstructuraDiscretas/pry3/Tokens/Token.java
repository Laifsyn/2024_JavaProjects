package com.utp.clsEstructuraDiscretas.pry3.Tokens;

import java.util.HashMap;
import java.util.Optional;

import com.utp.clsEstructuraDiscretas.pry3.Operator;
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
     * Convierte una subcadena en un token. Si se puede determinar el token,
     * devuelve el token.
     * <p>
     * En caso de no poder, pero se tiene una conjetura de qué es, devuelve
     * Token.EXPECTS({@code substring},?)
     * </p>
     * <p>
     * En caso de poder determinar que no es un token reconocido, devuelve
     * Token.UNEXPECTED({@code substring})
     * </p>
     * 
     * @param substring
     * @return
     */
    public static Token from_string(String substring) {
        if (substring.isEmpty()) {
            return new Token.EMPTY();
        }
        if (substring.matches(" +"))
            return new Token.BLANKSPACE(substring.length());
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
            return Token.from_string(subcadena.get());
        } else {
            // Si no hay coincidencias, entonces la subcadena no es un token conocido.
            return new Token.UNEXPECTED(substring);
        }
    }

    default boolean is_operator() {
        switch (this) {
            case Token.AND ignored -> {
            }
            case Token.OR ignored -> {
            }
            case Token.NOT ignored -> {
            }
            case Token.IMPLICATES ignored -> {
            }
            case Token.XAND ignored -> {
            }
            default -> {
                return false;
            }
        }
        return true;
    }

    public String toString();

    public String to_token_name();

    record AND(String inner_string) implements Token {
        @Override
        public String toString() {
            return "&";
        }

        @Override
        public String to_token_name() {
            return String.format("%s(\"%s\")", this.getClass().getSimpleName(), inner_string);
        }

    }

    record OR(String inner_string) implements Token {
        @Override
        public String toString() {
            return "|";
        }

        @Override
        public String to_token_name() {
            return String.format("%s(\"%s\")", this.getClass().getSimpleName(), inner_string);
        }

    }

    record NOT(String inner_string) implements Token {
        @Override
        public String toString() {
            return "~";
        }

        @Override
        public String to_token_name() {
            return String.format("%s(\"%s\")", this.getClass().getSimpleName(), inner_string);
        }
    }

    record IMPLICATES(String inner_string) implements Token {
        @Override
        public String toString() {
            return "->";
        }

        @Override
        public String to_token_name() {
            return String.format("%s(\"%s\")", this.getClass().getSimpleName(), inner_string);
        }
    }

    record XAND(String inner_string) implements Token {
        @Override
        public String toString() {
            return inner_string;
        }

        @Override
        public String to_token_name() {
            return String.format("%s(\"%s\")", this.getClass().getSimpleName(), inner_string);
        }
    }

    record IDENTIFIER(String ident) implements Token {
        @Override
        public String toString() {
            return "IDENTIFIER(" + ident + ")";
        }

        @Override
        public String to_token_name() {
            return String.format("%s(\"%s\")", this.getClass().getSimpleName(), ident);
        }
    }

    record OPEN_PAREN(char paren) implements Token {
        @Override
        public String toString() {
            return paren + "";
        }

        @Override
        public String to_token_name() {
            return String.format("%s(\"%s\")", this.getClass().getSimpleName(), paren);
        }

    }

    record CLOSE_PAREN(char paren) implements Token {
        @Override
        public String toString() {
            return paren + "";
        }

        @Override
        public String to_token_name() {
            return String.format("%s(\"%s\")", this.getClass().getSimpleName(), paren);
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

        @Override
        public String to_token_name() {
            int len = substring.length();
            if (len > 11)
                return String.format("%s(Size: %d)", this.getClass().getSimpleName(), len);
            else
                return String.format("%s(\"%s\")", this.getClass().getSimpleName(), substring);
        }
    }

    record EOL(String inner_string) implements Token {
        @Override
        public String toString() {
            return inner_string;
        }

        @Override
        public String to_token_name() {
            int len = inner_string.length();
            if (len > 11)
                return String.format("%s(Size: %d)", this.getClass().getSimpleName(), len);
            else
                return String.format("%s(\"%s\")", this.getClass().getSimpleName(), inner_string);
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

        @Override
        public String to_token_name() {
            String maybe_next = null;
            if (expects_next.isPresent()) {
                maybe_next = String.format("Some(\"%s\")", expects_next.get());
            } else {
                maybe_next = "None";
            }
            return String.format("%s(\"%s\", %s)", this.getClass().getSimpleName(), inner_string, maybe_next);
        }
    }

    record EMPTY() implements Token {
        @Override
        public String toString() {
            return "{EMPTY}";
        }

        @Override
        public String to_token_name() {
            return this.getClass().getSimpleName();
        }
    }

    record BLANKSPACE(int length) implements Token {
        @Override
        public String toString() {
            return " ".repeat(length);
        }

        @Override
        public String to_token_name() {
            return String.format("%s(%d)", this.getClass().getSimpleName(), length);
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
    static final String[] NOT = { "~", "¬", "!" };
    static final String[] IMPLICATES = { "->" };
    static final String[] XAND = { "<->" };
    static final String[][] DELIMITER = { { "(", ")" } };

    static final HashMap<String, Token> tokens_map = new HashMap<String, Token>() {
        {
            put(IDENT[0], new Token.IDENTIFIER(IDENT[0]));
            put(IDENT[1], new Token.IDENTIFIER(IDENT[1]));
            put(IDENT[2], new Token.IDENTIFIER(IDENT[2]));
            put(AND[0], new Token.AND(AND[0]));
            put(AND[1], new Token.AND(AND[1]));
            put(OR[0], new Token.OR(OR[0]));
            put(NOT[0], new Token.NOT(NOT[0]));
            put(NOT[1], new Token.NOT(NOT[1]));
            put(NOT[2], new Token.NOT(NOT[2]));
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

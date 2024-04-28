package com.utp.clsEstructuraDatos.pry3.Tokens;

import java.beans.Expression;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;

public sealed interface Token {
    public static Token fromString(String substring) {
        substring = substring.trim();
        if (substring.isEmpty()) {
            return new Token.EMPTY();
        }
        if (substring.length() > TokensGroup.max_chars) {
            return new Token.INVALID(new Exception("Token too long - Max length: " + TokensGroup.max_chars));
        }
        Token result = TokensGroup.tokens.get(substring);
        return result;

    }

    public String toString();

    record AND() implements Token {
        @Override
        public String toString() {
            return "&";
        }

    }

    record OR() implements Token {
        @Override
        public String toString() {
            return "|";
        }
    }

    record NOT() implements Token {
        @Override
        public String toString() {
            return "~";
        }
    }

    record IMPLICATES() implements Token {
        @Override
        public String toString() {
            return "->";
        }
    }

    record XAND() implements Token {
        @Override
        public String toString() {
            return "<->";
        }
    }

    /**
     * A token representing an expression. the inner value of the expression can be
     * evaluated into a bool.
     */
    record EXPRESSION(Optional<String[]> parentheses, Expression expression) implements Token {

        public EXPRESSION(Optional<String[]> parentheses, String expression) {
            if (parentheses.isPresent()) {
                String[] parens = parentheses.get();
                if (parens.length != 2) {
                    throw new IllegalArgumentException("Invalid parentheses: " + Arrays.toString(parens));
                }
                for (var valid_delimiters : TokensGroup.DELIMITER) {
                    if (valid_delimiters[0].equals(parens[0]) && valid_delimiters[1].equals(parens[1])) {
                        this.parentheses = parentheses;
                        this.expression = expression;
                        return;
                    }
                }
                throw new IllegalArgumentException("Invalid parentheses: " + Arrays.toString(parens)
                        + "%nExpected any of the following:" + Arrays.deepToString(TokensGroup.DELIMITER));
            } else {
                this.parentheses = Optional.empty();
                this.expression = expression;
            }
        }

        @Override
        public String toString() {
            String[] parentheses = this.parentheses.orElse(new String[] { "", "" });
            return "EXPRESSION(\"" + parentheses[0] + expression + parentheses[1] + "\")";
        }
    }

    record IDENTIFIER(String value) implements Token {
        @Override
        public String toString() {
            return "IDENTIFIER(" + value + ")";
        }
    }

    record UNKNOWN(String value) implements Token {
        @Override
        public String toString() {
            return "UNKNOWN(" + value + ")";
        }
    }

    record INVALID(Exception e) implements Token {
        @Override
        public String toString() {
            return e.getMessage();
        }
    }

    record EMPTY() implements Token {
        @Override
        public String toString() {
            return "{EMPTY}";
        }
    }

}

// public enum Token {
// AND, OR, NOT, IMPLICATES, XAND, PARENTHESE_OPEN, PARENTHESE_CLOSE,
// IDENTIFIER;
// }

class TokensGroup {
    static final int max_chars = 3;
    static final HashMap<String, Token> tokens = new HashMap<String, Token>() {
        {
            put(AND[0], new Token.AND());
            put(AND[1], new Token.AND());
            put(OR[0], new Token.OR());
            put(NOT[0], new Token.NOT());
            put(NOT[1], new Token.NOT());
            put(IMPLICATES[0], new Token.IMPLICATES());
            put(XAND[0], new Token.XAND());

            for (var delimiters : DELIMITER) {
                assert delimiters.length == 2 : delimiters.toString() + " isn't a valid delimiter - Expects 2 elements";
            }

            int static_members = count_members();
            int hashmap_size = size();
            assert hashmap_size == static_members : "TokensGroup size mismatches: " + hashmap_size + " != "
                    + static_members;
        }
    };

    static int count_members() {
        return AND.length + OR.length + NOT.length + IMPLICATES.length + XAND.length /* + DELIMITER.length * 2 */;
    }

    static final String[] AND = { "&", "^" };
    static final String[] OR = { "|" };
    static final String[] NOT = { "~", "¬" };
    static final String[] IMPLICATES = { "->" };
    static final String[] XAND = { "<->" };
    static final String[][] DELIMITER = { { "(", ")" } };
}

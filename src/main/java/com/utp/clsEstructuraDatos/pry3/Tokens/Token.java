package com.utp.clsEstructuraDatos.pry3.Tokens;

import java.util.HashMap;
import java.util.Optional;

public sealed interface Token {
    public final static int MAX_CHARS = TokensGroup.MAX_CHARS;

    public static Token fromString(String substring) {
        substring = substring.trim();
        if (substring.isEmpty()) {
            return new Token.EMPTY();
        }

        Token result = TokensGroup.tokens.get(substring);
        return result;

    }

    public String toString();

    public record AND(String inner_string) implements Token {
        @Override
        public String toString() {
            return "&";
        }

    }

    public record OR(String inner_string) implements Token {
        @Override
        public String toString() {
            return "|";
        }
    }

    public record NOT(String inner_string) implements Token {
        @Override
        public String toString() {
            return "~";
        }
    }

    public record IMPLICATES(String inner_string) implements Token {
        @Override
        public String toString() {
            return "->";
        }
    }

    public record XAND(String inner_string) implements Token {
        @Override
        public String toString() {
            return inner_string;
        }
    }



    // /**
    // * A token representing an expression. the inner value of the expression can
    // be
    // * evaluated into a bool.
    // */
    // record EXPRESSION(Optional<String[]> parentheses, Expression expression)
    // implements Token {

    // public EXPRESSION(Optional<String[]> parentheses, String expression) {
    // if (parentheses.isPresent()) {
    // String[] parens = parentheses.get();
    // if (parens.length != 2) {
    // throw new IllegalArgumentException("Invalid parentheses: " +
    // Arrays.toString(parens));
    // }
    // for (var valid_delimiters : TokensGroup.DELIMITER) {
    // if (valid_delimiters[0].equals(parens[0]) &&
    // valid_delimiters[1].equals(parens[1])) {
    // this.parentheses = parentheses;
    // this.expression = expression;
    // return;
    // }
    // }
    // throw new IllegalArgumentException("Invalid parentheses: " +
    // Arrays.toString(parens)
    // + "%nExpected any of the following:" +
    // Arrays.deepToString(TokensGroup.DELIMITER));
    // } else {
    // this.parentheses = Optional.empty();
    // this.expression = expression;
    // }
    // }

    // @Override
    // public String toString() {
    // String[] parentheses = this.parentheses.orElse(new String[] { "", "" });
    // return "EXPRESSION(\"" + parentheses[0] + expression + parentheses[1] +
    // "\")";
    // }
    // }

    public record IDENTIFIER(String ident) implements Token {
        @Override
        public String toString() {
            return "IDENTIFIER(" + ident + ")";
        }
    }

    public record OPEN_PAREN(char paren) implements Token {
        @Override
        public String toString() {
            return paren + "";
        }
    }

    public record CLOSE_PAREN(char paren) implements Token {
        @Override
        public String toString() {
            return paren + "";
        }
    }

    public record UNEXPECTED(String substring) implements Token {
        @Override
        public String toString() {
            return "`" + substring + "` is an invalid token";
        }
    }
    
    public record EOL(String inner_string) implements Token{
        @Override
        public String toString() {
            return inner_string;
        }
    }

    public record EXPECTS(String inner_string, Optional<String> expects_next) implements Token {
        @Override
        public String toString() {
            return "`" + inner_string + "` expects: `" + expects_next.orElseGet(() -> "{unknown}") + "`";
        }
    }

    public record EMPTY() implements Token {
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
    static final int MAX_CHARS = 3;
    static final HashMap<String, Token> tokens = new HashMap<String, Token>() {
        {
            put(AND[0], new Token.AND(AND[0]));
            put(AND[1], new Token.AND(AND[1]));
            put(OR[0], new Token.OR(OR[0]));
            put(NOT[0], new Token.NOT(NOT[0]));
            put(NOT[1], new Token.NOT(NOT[1]));
            put(IMPLICATES[0], new Token.IMPLICATES(IMPLICATES[0]));
            put(XAND[0], new Token.XAND(XAND[0]));

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

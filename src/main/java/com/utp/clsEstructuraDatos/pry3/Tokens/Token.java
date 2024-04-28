package com.utp.clsEstructuraDatos.pry3.Tokens;

public enum Token {
    AND, OR, NOT, IMPLICATES, XAND, PARENTHESE_OPEN, PARENTHESE_CLOSE, EXPRESSION;
}

class TokensGroup {
    final String[] AND = { "&", "^" };
    final String[] OR = { "|" };
    final String[] NOT = { "~", "¬" };
    final String[] IMPLICATES = { "->" };
    final String[] XAND = { "<->" };
    final String[][] DELIMITER = { { "(", ")" } };
}

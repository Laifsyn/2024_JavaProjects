package com.utp.clsEstructuraDatos.pry3.Tokens;


public enum Tokens {
    AND,
    OR,
    NOT,
    IMPLICATES,
    XAND,
    PARENTHESE_OPEN,
    PARENTHESE_CLOSE,
    EXPRESSION;

    static public Tokens Tokenize(){
        
        return AND;
    }
    public class Tokenize {
        
    }
}

class TokensGroup {
    final String[] AND = { "&", "^" };
    final String[] OR = { "|" };
    final String[] NOT = { "~", "¬" };
    final String[] IMPLICATES = { "->" };
    final String[] XAND = { "<->" };
    final String[][] DELIMITER = { { "(", ")" } };
}


package com.utp.clsEstructuraDatos.pry3;

public record Expression(String expression) {
    final static Character[] VARIABLES = { 'p', 'q', 'r' };

    // Constructor
    public Expression(String expression) {
        this.expression = expression;
    }

    // Getters
    public String getExpression() {
        return expression;
    }

    public boolean evaluate() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}

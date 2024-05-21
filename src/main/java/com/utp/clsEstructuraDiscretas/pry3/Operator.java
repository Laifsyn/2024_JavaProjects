package com.utp.clsEstructuraDiscretas.pry3;

import java.util.Optional;

import com.utp.clsEstructuraDiscretas.pry3.Tokens.Token;
import com.utp.utils.Result;

/**
 * <p>
 * Esta interfaz representa operadores lógicos, que pueden ser uno de los
 * siguientes:
 * </p>
 * <ul>
 * <li>AND: El resultado es verdadero si ambos operandos son verdaderos.</li>
 * <li>OR: El resultado es verdadero si al menos uno de los operandos es
 * verdadero.</li>
 * <li>NOT: El resultado es el opuesto del operando.</li>
 * <li>IMPLICATES: El resultado es falso solo si el primer operando es verdadero
 * y
 * el segundo operando es falso.</li>
 * <li>XAND: El Resultado es verdadero solo si ambas entradar son o Verdaderas o
 * Falsas
 * </ul>
 * 
 */
public enum Operator {
    AND, OR, NOT, IMPLICATES, XAND;

    /**
     * <p>
     * Este método devuelve el operador lógico correspondiente a la cadena de texto
     * proporcionada.
     * </p>
     * 
     * @param operator
     *            Cadena de texto que representa un operador lógico.
     * @return Operador lógico correspondiente a la cadena de texto proporcionada.
     */
    public static Result<Operator, Token> fromToken(Token token) {

        Result<Operator, Token> ops = switch (token) {
            case Token.AND(var _unused) -> Result.ok(AND);
            case Token.OR(var _unused) -> Result.ok(OR);
            case Token.NOT(var _unused) -> Result.ok(NOT);
            case Token.IMPLICATES(var _unused) -> Result.ok(IMPLICATES);
            case Token.XAND(var _unused) -> Result.ok(XAND);
            default -> Result.error(token);
        };
        return ops;
    }

    // Operator Overloading
    public static boolean eval(boolean lhs, Operator op, Optional<Boolean> rhs) {
        return op.eval(lhs, rhs);
    }

    public boolean eval(boolean lhs, Optional<Boolean> maybe_rhs) {
        boolean rhs = false;
        if (this != Operator.NOT && maybe_rhs.isEmpty()) {
            throw new IllegalArgumentException("No second input was provided when Operator was " + this);
        }
        if (maybe_rhs.isPresent()) {
            rhs = maybe_rhs.get();
        }

        boolean op = switch (this) {
            case AND -> (lhs & rhs);
            case OR -> (lhs | rhs);
            case NOT -> (!lhs);
            case IMPLICATES -> (!lhs | rhs);
            case XAND -> (lhs == rhs);
        };
        return op;
    }

    public String display() {
        return switch (this) {
            case AND -> "&";
            case OR -> "|";
            case NOT -> "!";
            case IMPLICATES -> "->";
            case XAND -> "<->";
        };
    }

}

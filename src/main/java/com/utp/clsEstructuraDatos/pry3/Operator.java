package com.utp.clsEstructuraDatos.pry3;

import java.util.Optional;

import com.utp.clsEstructuraDatos.pry3.Tokens.Token;
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

    public boolean eval(boolean rhs, Optional<Boolean> maybe_lhs) {
        boolean lhs = false;
        if (this != Operator.NOT && maybe_lhs.isEmpty()) {
            throw new IllegalArgumentException("No second input was provided when Operator was " + this);
        }
        if (maybe_lhs.isPresent()) {
            lhs = maybe_lhs.get();
        }

        boolean op = switch (this) {
            case AND -> (rhs && lhs);
            case OR -> (rhs || lhs);
            case NOT -> (!rhs);
            case IMPLICATES -> (!lhs || rhs);
            case XAND -> (rhs == lhs);
        };
        return op;
    }

}

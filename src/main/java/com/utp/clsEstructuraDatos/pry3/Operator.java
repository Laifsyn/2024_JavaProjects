package com.utp.clsEstructuraDatos.pry3;

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
 * </ul>
 * 
 * Enumeraciones que contienen datos.
 * Basado en esta implementación en reddit por `NitronHX`.
 * 
 * @see <a href=
 *      "https://www.reddit.com/r/java/comments/135i37c/rust_like_enums_in_java/">{@literal
 *      Reddit Post on Result< T >}</a>
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
            case Token.AND() -> Result.ok(AND);
            case Token.OR() -> Result.ok(OR);
            case Token.NOT() -> Result.ok(NOT);
            case Token.IMPLICATES() -> Result.ok(IMPLICATES);
            case Token.XAND() -> Result.ok(XAND);
            default -> Result.error(token);
        };
        return ops;
    }
}

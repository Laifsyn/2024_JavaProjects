package com.utp.clsEstructuraDatos.pry3;

/**
 * <p>
 * Esta interfaz representa operadores lógicos, que pueden ser uno de los
 * siguientes:
 * </p>
 * <ul>
 * <li>AND: El resultado es verdadero si ambos operandos son verdaderos.</li>
 * <li>OR: El resultado es verdadero si al menos uno de los operandos es verdadero.</li>
 * <li>NOT: El resultado es el opuesto del operando.</li>
 * <li>IMPLICATES: El resultado es falso solo si el primer operando es verdadero y
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
public sealed interface Operators {
    public boolean eval();

    record AND(Expression lhs, Expression rhs) implements Operators {
        @Override
        public boolean eval() {
            return lhs.evaluate() && rhs.evaluate();
        }
    }

    record OR(Expression lhs, Expression rhs) implements Operators {
        @Override
        public boolean eval() {
            return lhs.evaluate() || rhs.evaluate();
        }
    }

    /**
     * El operador IMPLICATES representa la operación de implicación lógica.
     * La tabla de verdad para esta operación es la siguiente:
     *
     * <pre>
     * Tabla de verdad de `Implicates`
     * | lhs | rhs | lhs -> rhs |
     * |-----|-----|------------|
     * | T   | T   | T          |
     * | T   | F   | F          |
     * | F   | T   | T          |
     * | F   | F   | T          |
     * Explicación:
     * <ul>
     * <li>- Si tanto `lhs` como `rhs` son verdaderos, el resultado es
     * verdadero.</li>
     * <li>- Si `lhs` es verdadero y `rhs` es falso, el resultado es falso.</li>
     * <li>- Si `lhs` es falso, el resultado es verdadero independientemente del
     * valor de `rhs`.</li>
     * </ul>
     * </pre>
     *
     * 
     * @return el resultado de la operación de implicación lógica
     */
    record IMPLICATES(Expression lhs, Expression rhs) implements Operators {
        @Override
        public boolean eval() {

            return !lhs.evaluate() || rhs.evaluate();
        }
    }

    record XAND(Expression lhs, Expression rhs) implements Operators {
        @Override
        public boolean eval() {
            return !(lhs.evaluate() ^ rhs.evaluate());
        }
    }

    record NOT(Expression expression) implements Operators {
        @Override
        public boolean eval() {
            return !expression.evaluate();
        }
    }
}

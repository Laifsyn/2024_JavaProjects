package com.utp.clsEstructuraDatos.pry3;

/**
 * <p>
 * This interface represents logical operators, which can be one of the
 * following:
 * </p>
 * <ul>
 * <li>AND: The result is true if both operands are true.</li>
 * <li>OR: The result is true if at least one operand is true.</li>
 * <li>NOT: The result is the opposite of the operand.</li>
 * <li>IMPLICATES: The result is false only if the first operand is true and
 * the second operand is false.</li>
 * </ul>
 * 
 * Enums that holds data.
 * Based on this implementation in reddit by `NitronHX`.
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
     * The IMPLICATES operator represents the logical implication operation.
     * The truth table for this operation is as follows:
     *
     * <pre>
     * `Implicates` Truth table
     * | lhs | rhs | lhs -> rhs |
     * |-----|-----|------------|
     * | T   | T   | T          |
     * | T   | F   | F          |
     * | F   | T   | T          |
     * | F   | F   | T          |
     * </pre>
     *
     * Explanation:
     * - If both `lhs` and `rhs` are true, the result is true.
     * - If `lhs` is true and `rhs` is false, the result is false.
     * - If `lhs` is false, the result is true regardless of the value of `rhs`.
     *
     * @return the result of the logical implication operation
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

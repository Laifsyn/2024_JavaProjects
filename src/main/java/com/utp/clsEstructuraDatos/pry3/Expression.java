package com.utp.clsEstructuraDatos.pry3;

import java.util.HashMap;
import java.util.Optional;

public sealed interface Expression {
    boolean eval(HashMap<String, Boolean> boolean_map);

    record IDENTIFIER(String ident) implements Expression {

        @Override
        public boolean eval(HashMap<String, Boolean> boolean_map) {
            Boolean answer = boolean_map.get(ident);
            if (answer == null) {
                throw new RuntimeException("Miembro " + ident + " no existe en el mapa");
            }
            return answer;
        }

    }

    record NEGATING(Expression exp) implements Expression {

        @Override
        public boolean eval(HashMap<String, Boolean> boolean_map) {
            return (!exp.eval(boolean_map));
        }
    }

    record LR_EXPRESSION(Expression lhs, Operator op, Expression rhs) implements Expression {

        @Override
        public boolean eval(HashMap<String, Boolean> boolean_map) {
            return op.eval(lhs.eval(boolean_map), Optional.of(rhs.eval(boolean_map)));
        }
    }
}

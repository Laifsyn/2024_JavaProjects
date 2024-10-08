package com.utp.clsEstructuraDiscretas.pry3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Optional;

import com.utp.clsEstructuraDiscretas.pry3.Tokens.Token;
import com.utp.clsEstructuraDiscretas.pry3.Tokens.TokenStream;
import com.utp.utils.Result;

public sealed interface Expression {
    boolean eval(HashMap<String, Boolean> boolean_map);

    String display();

    HashSet<String> get_identifiers();

    /**
     * Se usa un LinkedHashMap para conservar orden de insercion. Los
     * identificadores tenderan a ser los primeros en ser encontrados en este mapa.
     * 
     * Se guarda en la entrada la direccion de la expresion que se crea.
     */
    public void load_expressions(LinkedHashMap<String, Expression> expressions);

    default String as_printable_table() {
        HashSet<String> identificadores = this.get_identifiers();
        // Cargamos primero los identificadores del mapa de valores, y la lista de
        // expresiones
        LinkedHashMap<String, Expression> expressions = new LinkedHashMap<String, Expression>();
        HashMap<String, Boolean> lista_de_variables = new HashMap<>();
        for (String ident : identificadores) {
            expressions.put(ident, null);
            lista_de_variables.put(ident, false);
        }
        // Cargamos el resto de las expresiones
        this.load_expressions(expressions);

        ArrayList<String> fila = new ArrayList<>();
        for (String expression_as_string : expressions.keySet()) {
            fila.add(expression_as_string);
        }
        // Clonamos a un encabezado como constante porque lo vamos a usar para lectura
        // solamente
        final String[] encabezado = fila.toArray(new String[] {});

        DrawTable tabla = new DrawTable();
        tabla.insertar_fila(encabezado);

        // Calculamos los posibles estados de la combinacion de variables
        int states = (int) Math.round(Math.pow(2, identificadores.size()));
        // fila_n es la fila del contenido
        for (int fila_n = 0; fila_n < states; fila_n++) {
            // Quiero asegurarme que estoy trabajando con fila vacia.
            fila.clear();

            // Cargamos los valores de las variables usando bit shifting.
            int estado_de_variables = 0;
            // Invertimos el orden de los bits a LE
            // i.e. Si ultimos 3 bits de fila_n == 110, entonces estado_de_variables == 011
            for (int i = 0; i < identificadores.size(); i++) {
                estado_de_variables = (estado_de_variables << 1) | ((fila_n >> i) & 1);
            }
            for (Entry<String, Boolean> entry : lista_de_variables.entrySet()) {
                entry.setValue(((estado_de_variables & 1) == 1));
                estado_de_variables = estado_de_variables >> 1;
            }
            // Ya se puede empezar a llenar las filas
            for (String expresion_a_evaluar : encabezado) {
                Expression exp = expressions.get(expresion_a_evaluar);
                if (exp == null) {
                    fila.add("null");
                } else {
                    fila.add(exp.eval(lista_de_variables) ? "1" : "0");
                }
            }
            tabla.insertar_fila(fila.toArray(new String[] {}));
        }
        return tabla.toString();
    }

    /**
     * Retorna al primer error encontrado. Posiblemente con un util mensaje de error
     */
    public static Result<Expression, String> try_from_stream(final TokenStream stream) {
        Optional<Result<Expression, String>> expression = try_next_expression(stream, 0);
        if (expression.isEmpty()) {
            return Result.error("No se encontraron expresiones");
        }
        if (expression.get().isError()) {
            return Result.error(expression.get().unwrapError());
        }
        return Result.ok(expression.get().unwrapOk());
    }

    private static Optional<Result<Expression, String>> try_next_expression(TokenStream stream, final int nest_level) {

        /**
         * Sintaxis de las expresiones:
         * Identificadores son Expresiones ya que se pueden evaluar a un Verdadero o
         * Falso
         * Despues de una expresión puede seguir un operador, o nada. (`...->p` ó
         * `p->...`)
         * Al abrir paréntesis, puede haber una expresión, consecuentemente anidándola.
         * Después de la expresión anidada, tiene que estar encerrada por un paréntesis.
         * Al cerrar paréntesis, puede seguir un operador, o nada.
         * Al encontrar un operador, tiene que haber una expresión a la derecha. Solo el
         * Negar puede venir antes que la expresión
         */
        Optional<Token> optional_token = stream.next();
        // Expression exp = null;
        ExpressionBuilder exp_builder = new ExpressionBuilder.EMPTY();
        Token last_operator_token = new Token.EMPTY();
        while (optional_token.isPresent()) {
            Token token = optional_token.get();
            // System.out.println(stream.position() + ", " + nest_level + ")DEBUG: " +
            // exp_builder.class_name() + " - " +
            // token.to_token_name());
            // Early return for errors
            switch (token) {
                case Token.BLANKSPACE ignored -> {
                    optional_token = stream.next();
                    continue;
                }
                case Token.UNEXPECTED(String unexpected) -> {
                    return Optional.of(Result.error("Token inesperado: " + unexpected));
                }
                case Token.EMPTY() -> {
                    return Optional.of(Result.error("Token vacío"));
                }
                case Token.EOL(String content) -> {
                    return Optional.of(Result.error("Fin de la línea: " + content.length()));
                }
                case Token.EXPECTS expected_token -> {
                    return Optional.of(Result.error("Se esperaba un token: `" + expected_token.toString() + "`"));
                }
                case Token.CLOSE_PAREN ignored -> {
                    // System.out.println("Close Paren found");
                    /**
                     * La expresión ya se debe construir para cuando se encuentre un cierre de
                     * paréntesis
                     */
                    if (nest_level == 0) {
                        return Optional.of(Result.error(
                                "Cierre de Parentesis encontrado sin apertura respectiva : char#" + stream.position()));
                    }
                    switch (exp_builder) {
                        case ExpressionBuilder.LR_EXPRESSION expression -> {
                            return Optional.of(expression.build());
                        }
                        case ExpressionBuilder.NEGATING expression -> {
                            return Optional.of(expression.build());
                        }
                        case ExpressionBuilder.EMPTY _ignored -> {
                            return Optional.of(Result.error("Parentesis vacio"));
                        }
                        case ExpressionBuilder.SingleExpression expression -> {
                            return Optional.of(expression.build());
                        }
                        default -> {
                            return Optional.of(Result.error("Builder " + exp_builder.class_name()
                                    + " isn't expecting a closing parenthesis"));
                        }
                    }
                }
                default -> {
                    // No hacer nada
                }
            }

            if (token instanceof Token.OPEN_PAREN) {
                var next_exp = try_next_expression(stream, nest_level + 1);

                if (next_exp.isEmpty()) {
                    return Optional.of(Result.error("Parentesis abierto sin cierre"));
                }
                if (next_exp.get().isError()) {
                    return next_exp;
                }
                var next_expression = next_exp.get().unwrapOk();
                if (last_operator_token instanceof Token.NOT
                        && exp_builder instanceof ExpressionBuilder.IntoLR_Expression) {
                    next_expression = new Expression.NEGATING(next_expression);
                }
                var insert_result = try_insert_exp(exp_builder, next_expression);
                if (insert_result.isError()) {
                    return Optional.of(Result.error(insert_result.unwrapError()));
                }
                exp_builder = insert_result.unwrapOk();
                // System.out.println("Debug, Returning a: " + exp_builder.build().unwrapOk());
                // System.out.println("Debug, Returning a: " + exp_builder.class_name());
            }
            if (token.is_operator()) {
                // Inserta el operador
                Operator op = Operator.fromToken(token).unwrapOk();
                if (op == Operator.NOT) {
                    switch (exp_builder) {
                        case ExpressionBuilder.EMPTY exp -> {
                            exp_builder = new ExpressionBuilder.IntoNegating();
                        }
                        case ExpressionBuilder.IntoLR_Expression exp -> {
                            // Do Nothing
                        }
                        // case ExpressionBuilder.NEGATING exp -> {
                        // exp_builder = exp.append(op);
                        // }
                        default -> {
                            return Optional.of(Result.error(
                                    String.format("Operador `%s` usado sin expresión acompañante: %s",
                                            token.to_token_name(), exp_builder.class_name())));
                        }
                    }
                } else {
                    switch (exp_builder) {
                        case ExpressionBuilder.SingleExpression exp -> {
                            exp_builder = exp.append(op);
                        }
                        case ExpressionBuilder.LR_EXPRESSION exp -> {
                            exp_builder = exp.append(op);
                        }
                        case ExpressionBuilder.NEGATING exp -> {
                            exp_builder = exp.append(op);
                        }
                        default -> {
                            return Optional.of(Result.error(
                                    String.format("Operador `%s` usado sin expresión acompañante: %s",
                                            token.to_token_name(), exp_builder.class_name())));
                        }
                    }
                }
            } else if (token instanceof Token.IDENTIFIER(String identfier)) {
                // Inserta expresión de IDENTIFIER
                Expression insert_exp = new Expression.IDENTIFIER(identfier);
                if (last_operator_token instanceof Token.NOT
                        && exp_builder instanceof ExpressionBuilder.IntoLR_Expression) {
                    insert_exp = new Expression.NEGATING(insert_exp);
                }
                var insert_result = try_insert_exp(exp_builder, insert_exp);
                if (insert_result.isError()) {
                    return Optional.of(Result.error(insert_result.unwrapError()));
                }
                exp_builder = insert_result.unwrapOk();
            }
            if (token.is_operator()) {
                last_operator_token = token;
            }
            optional_token = stream.next();
        }
        // System.out.println("Debug, Returning a: " + exp_builder.class_name());
        switch (exp_builder) {
            case ExpressionBuilder.EMPTY _ignored -> {
                return Optional.of(Result.error("No se encontró ninguna expresión"));
            }
            case ExpressionBuilder.IntoNegating _ignored -> {
                return Optional.of(Result.error("Negación sin expresión"));
            }
            case ExpressionBuilder.IntoLR_Expression _ignored -> {
                return Optional.of(Result.error("Expresión incompleta"));
            }
            case ExpressionBuilder.SingleExpression expression -> {
                return Optional.of(expression.build());
            }
            case ExpressionBuilder.LR_EXPRESSION expression -> {
                return Optional.of(expression.build());
            }
            case ExpressionBuilder.NEGATING expression -> {
                return Optional.of(expression.build());
            }
        }
    }

    /**
     * Avanza el Constructor, de lo contrario, devuelve un error relacionado a que
     * en esta etapa no espera recibir expresión
     */
    private static Result<ExpressionBuilder, String> try_insert_exp(ExpressionBuilder exp_builder,
            Expression exp) {
        switch (exp_builder) {
            case ExpressionBuilder.EMPTY ignored -> {
                exp_builder = new ExpressionBuilder.SingleExpression(exp);
            }
            case ExpressionBuilder.IntoLR_Expression expression -> {
                exp_builder = expression.append(exp).unwrapOk();
            }
            case ExpressionBuilder.IntoNegating expression -> {
                exp_builder = expression.append(exp).unwrapOk();
            }
            case ExpressionBuilder.SingleExpression _ignored -> {
                return Result.error("`SingleExpression` isn't expecting an Expression: `" + exp.display()
                        + "`, but either being built or an operator");
            }
            default -> {
                return Result.error("Builder `" + exp_builder.class_name()
                        + "` isn't expecting an Expression: `" + exp.display() + "`");
            }
        }
        return Result.ok(exp_builder);
    }

    record IDENTIFIER(String ident) implements Expression {
        @Override
        public boolean eval(HashMap<String, Boolean> boolean_map) {
            Boolean answer = boolean_map.get(ident);
            if (answer == null) {
                throw new RuntimeException("Miembro " + ident + " no existe en el mapa");
            }
            return answer;
        }

        public String display() {
            return ident;
        }

        @Override
        public HashSet<String> get_identifiers() {
            var idents = new HashSet<String>();
            idents.add(this.ident);
            return idents;
        }

        @Override
        public void load_expressions(LinkedHashMap<String, Expression> expressions) {
            expressions.putIfAbsent(this.display(), this);
        }

    }

    record NEGATING(Expression exp) implements Expression {

        @Override
        public boolean eval(HashMap<String, Boolean> boolean_map) {
            return (!exp.eval(boolean_map));
        }

        public String display() {
            // return "(" + lhs.display() + op.display() + rhs.display() + ")";
            StringBuilder sb = new StringBuilder();
            sb.append("!");
            append_expression(sb, exp);
            return sb.toString();
        }

        @Override
        public HashSet<String> get_identifiers() {
            return exp.get_identifiers();
        }

        @Override
        public void load_expressions(LinkedHashMap<String, Expression> expressions) {
            this.exp.load_expressions(expressions);
            expressions.putIfAbsent(this.display(), this);
        }
    }

    record LR_EXPRESSION(Expression lhs, Operator op, Expression rhs) implements Expression {

        @Override
        public boolean eval(HashMap<String, Boolean> boolean_map) {
            // if (op == Operator.IMPLICATES) {
            // System.out.println(boolean_map);
            // System.out.println(lhs + " " + op + " " + rhs + "_______");
            // System.out.println(lhs.eval(boolean_map) + " " + op + " "
            // + rhs.eval(boolean_map) + " = "
            // + op.eval(lhs.eval(boolean_map), Optional.of(rhs.eval(boolean_map))) + "\n");
            // }
            return op.eval(lhs.eval(boolean_map), Optional.of(rhs.eval(boolean_map)));
        }

        public String display() {
            // return "(" + lhs.display() + op.display() + rhs.display() + ")";
            StringBuilder sb = new StringBuilder();
            append_expression(sb, lhs);
            sb.append(op.display());
            append_expression(sb, rhs);
            return sb.toString();
        }

        @Override
        public HashSet<String> get_identifiers() {
            var idents = lhs.get_identifiers();
            idents.addAll(rhs.get_identifiers());
            return idents;
        }

        @Override
        public void load_expressions(LinkedHashMap<String, Expression> expressions) {
            lhs.load_expressions(expressions);
            rhs.load_expressions(expressions);
            expressions.putIfAbsent(this.display(), this);
        }

    }

    static private void append_expression(StringBuilder sb, Expression exp) {
        if (exp instanceof LR_EXPRESSION) {
            sb.append("(");
            sb.append(exp.display());
            sb.append(")");
        } else {
            sb.append(exp.display());
        }
    }
}

sealed interface ExpressionBuilder {

    /**
     * Retorna el nombre de la clase/instancia
     */
    default public String class_name() {
        return this.getClass().getSimpleName();
    }

    /**
     * Construye la expresión a partir del estado actual del Constructor
     * 
     * @return
     */
    default public Result<Expression, String> build() {
        return Result.error("No se puede construir expresion a partir del estado actual del Constructor: "
                + this.getClass().getSimpleName());
    };

    /**
     * Agrega una expresión al constructor
     * 
     */
    default Result<ExpressionBuilder, String> append(Expression exp) {
        return Result.error("No se puede agregar este elemento a partir del estado actual del Constructor: "
                + this.getClass().getSimpleName());
    };

    /**
     * El Constructor espera recibir una expresión, o el operador de negar
     */
    record EMPTY() implements ExpressionBuilder {

        public Result<ExpressionBuilder, String> append(Expression exp) {
            return Result.ok(new ExpressionBuilder.SingleExpression(exp));
        }

        public Result<ExpressionBuilder, String> append(Operator op) {
            if (Operator.NOT == op) {
                return Result.ok(new ExpressionBuilder.IntoNegating());
            } else {
                return Result.error("Solo se puede inicializar con Operador NOT");
            }
        }
    }

    /**
     * El Constructor espera recibir una expresión para poder negar
     */
    record IntoNegating() implements ExpressionBuilder {
        public Result<ExpressionBuilder, String> append(Expression exp) {
            return Result.ok(new ExpressionBuilder.NEGATING(exp));
        }
    };

    /**
     * El Constructor ya se puede construir
     */
    record NEGATING(Expression exp) implements ExpressionBuilder {
        @Override
        public Result<Expression, String> build() {
            return Result.ok(new Expression.NEGATING(exp));
        }

        public ExpressionBuilder append(Operator op) {
            return new ExpressionBuilder.IntoLR_Expression(new Expression.NEGATING(exp), op);
        }
    }

    /**
     * El constructor espera recibir un operador para comparar con otra expresión
     * (Resuelve a IntoLR_Expression)
     * El constructor se puede construir en este estado.
     */
    record SingleExpression(Expression exp) implements ExpressionBuilder {
        public ExpressionBuilder append(Operator op) {
            return new ExpressionBuilder.IntoLR_Expression(exp, op);
        }

        @Override
        public Result<Expression, String> build() {
            return Result.ok(exp);
        }
    }

    /**
     * El constructor espera recibir una expresión. (Resuelve a LR_EXPRESSION)
     */
    record IntoLR_Expression(Expression lhs, Operator op) implements ExpressionBuilder {
        public Result<ExpressionBuilder, String> append(Expression exp) {
            return Result.ok(new ExpressionBuilder.LR_EXPRESSION(lhs, op, exp));
        }
    }

    /**
     * El constructor ya se puede construir
     */
    record LR_EXPRESSION(Expression lhs, Operator op, Expression rhs) implements ExpressionBuilder {
        public ExpressionBuilder append(Operator op) {
            return new ExpressionBuilder.IntoLR_Expression(this.build().unwrapOk(), op);
        }

        @Override
        public Result<Expression, String> build() {
            return Result.ok(new Expression.LR_EXPRESSION(lhs, op, rhs));
        }
    }
}

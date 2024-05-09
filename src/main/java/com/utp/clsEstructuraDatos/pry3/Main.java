package com.utp.clsEstructuraDatos.pry3;

import java.util.ArrayList;
import java.util.Optional;

import com.utp.clsEstructuraDatos.pry3.Tokens.Token;
//  * Desarrollar un programa en lenguaje C/C++ o Java que elabore 
//  * una tabla de verdad en base a una expresión lógica considerando 
//  * los operadores de negación, inclusión, disyunción, condicional,
//  * y bicondicional. Considere un máximo de 3 variables (p, q, r).   
//  * 
//  * El programa no será interactivo en la entrada, ya que deberá 
//  * indicar la expresión lógica a evaluar mediante código. 
import com.utp.clsEstructuraDatos.pry3.Tokens.TokenStream;
import com.utp.utils.Result;

public class Main {
    public static void main(String[] args) {
        // Result<TokenStream, IllegalArgumentException> stream =
        // TokenStream.from_string("pqr(^^&&()|||)");
        String[] input = new String[] {
                "p|q",
                "p&q",
                "(p&q)^r|(p^q)",
                "(!(p->q) ->~q)&p",
                "(!p->q ->~q)&p",
                "(!(!p->!q) ->!(!q^!(p->q)))&!p",

        };

        for (String string : input) {
            System.out.println("Evaluating: `" + string + "`");
            TokenStream stream = TokenStream.from_string(string).unwrapOk();
            Result<Expression, String> expression = Expression.try_from_stream(stream);
            if (expression.isError()) {
                System.err.println("Error: " + expression.unwrapError());
                System.err.println("Stream: " + stream.position());
                System.err.println("Stream: " + stream.next().orElseGet(() -> new Token.EOL("asdasd")).to_token_name());
            } else {
                System.out.println("Exito!!");
                System.err.println("Evaluating: " + expression.unwrapOk().display());
            }

            System.err.println("\n<====================================>");
        }

    }

}

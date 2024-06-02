package com.utp.clsEstructuraDiscretas.pry3;

import com.utp.clsEstructuraDiscretas.pry3.Tokens.Token;
import com.utp.clsEstructuraDiscretas.pry3.Tokens.TokenStream;
import com.utp.utils.Result;

//  * Desarrollar un programa en lenguaje C/C++ o Java que elabore 
//  * una tabla de verdad en base a una expresión lógica considerando 
//  * los operadores de negación, inclusión, disyunción, condicional,
//  * y bicondicional. Considere un máximo de 3 variables (p, q, r).   
//  * 
//  * El programa no será interactivo en la entrada, ya que deberá 
//  * indicar la expresión lógica a evaluar mediante código. 
public class Main {
    public static void main(String[] args) {
        String[] entries = new String[] {
                "p|q",
                "p|q&r",
                "(p&q)^r|(p^q)",
                "((p->~q)^(r^(q->p))->~r"
        };
        int entry_no = 0;
        for (String input : entries) {
            entry_no++;
            System.out.println(entry_no + "-)Evaluating: `" + input + "`");

            // Convertir la Cadena a un productor de Token
            TokenStream stream = TokenStream.from_string(input).unwrapOk();
            Result<Expression, String> expression = Expression.try_from_stream(stream);
            // La cadena de Tokens tiene la posibilidad de no ser una expresión válida.
            if (expression.isError()) {
                System.err.println("Error: " + expression.unwrapError());
                System.err.println("Stream: " + stream.position());
                System.err.println("Stream: " + stream.next().orElseGet(() -> new Token.EOL("")).to_token_name());
                continue;
            }
            var valid_exp = expression.unwrapOk();
            System.out.println("Tabla de Verdad:\n" + valid_exp.as_printable_table());
        }

    }

}

package com.utp.clsEstructuraDatos.pry3;

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

public class Main {
    public static void main(String[] args) {
        // Result<TokenStream, IllegalArgumentException> stream =
        // TokenStream.from_string("pqr(^^&&()|||)");
        String input = "pqr(^^-><<->~!&&  ()   | ||)   ![((p<->((q^q)^r))->q)^~p]";
        System.out.println(input);
        TokenStream stream = TokenStream.from_string(input).unwrapOk();
        Optional<Token> token = stream.next();
        while (token.isPresent()) {
            if (token.get() instanceof Token.BLANKSPACE(int _ignored))
                continue;
            if (token.get() instanceof Token.UNEXPECTED(String _ignored)){
                System.out.println(token.get().to_token_name());
                break;}
            System.out.println(token.get().to_token_name());
            token = stream.next();
        }

    }

}

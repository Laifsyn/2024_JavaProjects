package com.utp.clsEstructuraDatos.pry3;

import com.utp.clsEstructuraDatos.pry3.Tokens.Tokens;
import com.utp.clsEstructuraDatos.pry3.utils.Either;

public class Tokenizer {
    static Either<Tokens, String> tokenize(String input){
        return Either.right(input);
    }
}

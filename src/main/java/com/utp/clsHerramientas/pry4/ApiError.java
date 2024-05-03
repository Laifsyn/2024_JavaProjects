package com.utp.clsHerramientas.pry4;

public sealed interface ApiError<T> {
    String get_message();

    public record EntradaInvalida(String entrada, String contexto) implements ApiError<String> {

        @Override
        public String get_message() {
            return "Entrada inválida: " + entrada + "\n - " + contexto;
        }
    }

    public record OperacionCancelada<T>() implements ApiError<T> {

        @Override
        public String get_message() {
            return "Operación cancelada";
        }
    }

    public record Otro<T>(String msg) implements ApiError<T> {

        @Override
        public String get_message() {
            return msg;
        }
    }
}

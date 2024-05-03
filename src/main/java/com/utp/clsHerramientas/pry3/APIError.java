package com.utp.clsHerramientas.pry3;

public sealed interface APIError {
    String getMessage();

    record OperacionCancelada() implements APIError {
        @Override
        public String getMessage() {
            return "Operación cancelada";
        }
    }

    record IntentoAgotado() implements APIError {
        @Override
        public String getMessage() {
            return "Agotó los intentos de acceso";
        }
    }

    record ContraseñaIncorrecta() implements APIError {
        @Override
        public String getMessage() {
            return "Contraseña incorrecta";
        }
    }
}
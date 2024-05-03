package com.utp.clsHerramientas.pry4;

import com.utp.utils.Result;
import com.utp.clsHerramientas.pry3.clsPassword;
import com.utp.clsHerramientas.pry3.APIError;
import com.utp.clsHerramientas.pry3.APIError.OperacionCancelada;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.swing.JOptionPane;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello World");
        var login_result = Main.login();
        if (login_result.isError()) {
            JOptionPane.showMessageDialog(null, login_result.unwrapError().get_message(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        int opcion = Opciones.menu();
        switch (opcion) {
            case 0 -> {
                var result = Opciones.Opcion1_pendiente();
                JOptionPane.showMessageDialog(null, "La pendiente es: " + result, "Resultado",
                        JOptionPane.INFORMATION_MESSAGE);
            }
            case 1 -> {
                var result = Opciones.Opcion2_punto_medio();
                JOptionPane.showMessageDialog(null, "El punto medio es: " + result, "Resultado",
                        JOptionPane.INFORMATION_MESSAGE);
            }
            case 2 -> {
                var result = Opciones.Opcion3_raiz_cuadratica();
                if (result.isEmpty())
                    return;
                BigDecimal raiz1 = result.get()[0];
                BigDecimal raiz2 = result.get()[1];
                JOptionPane.showMessageDialog(null,
                        "Las raices son: x_1=" + raiz1.setScale(4, RoundingMode.HALF_EVEN) + " y x_2="
                                + raiz2.setScale(4, RoundingMode.HALF_EVEN),
                        "Resultado",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    static <T, E> Result<T, ApiError<E>> login() {
        clsPassword user_login = clsPassword.get_user();
        Result<char[], OperacionCancelada> password;
        while (true) {
            password = user_login.read_password();
            if (password.isError())
                return Result.error(new ApiError.OperacionCancelada<>());
            var login_result = user_login.login(password.unwrapOk());
            if (login_result.isError()) {
                switch (login_result.unwrapError()) {
                    case APIError.IntentoAgotado() -> {
                        return Result.error(
                                new ApiError.Otro<>("Lo siento " + user_login.usuario + " hable con su supervisor."));
                    }
                    case APIError.ContraseñaIncorrecta error -> {
                        JOptionPane.showMessageDialog(null,
                                "Contraseña incorrecta. Intente denuevo - Le quedan " + user_login.intentos()
                                        + " intentos");
                        continue;
                    }
                    case APIError.OperacionCancelada() -> {
                        System.out.println("Operación cancelada");
                        return Result.error(new ApiError.OperacionCancelada<E>());
                    }
                }
            }
            JOptionPane.showMessageDialog(null,
                    "Bienvenido " + user_login.usuario + ". Gracias por utilizar nuestro software");
            return Result.ok(null);
        }
    }
}
package com.utp.clsHerramientas.pry3;

import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

import com.utp.clsHerramientas.pry3.APIError.OperacionCancelada;
import com.utp.utils.Result;

public class clsPassword {
    public static void main(String[] args) {
        clsPassword user_login = get_user();
        Result<char[], OperacionCancelada> input;
        while (true) {
            input = user_login.read_password();
            if (input.isError()) {
                JOptionPane.showMessageDialog(null, input.unwrapError().getMessage());
                return;
            }
            Result<String, APIError> login_result = user_login.login(input.unwrapOk());
            if (login_result.isOk()) {
                JOptionPane.showMessageDialog(null,
                        "Bienvenido " + user_login.usuario + ". Gracias por utilizar nuestro software");
                break;
            }
            switch (login_result.unwrapError()) {
                case APIError.IntentoAgotado() -> {
                    JOptionPane.showMessageDialog(null,
                            "Lo siento " + user_login.usuario + " hable con su supervisor.");
                    return;
                }
                case APIError.ContraseñaIncorrecta error -> JOptionPane.showMessageDialog(null,
                        "Contraseña incorrecta. Intente denuevo - Le quedan " + user_login.intentos() + " intentos");
                case APIError.OperacionCancelada() -> {
                    JOptionPane.showMessageDialog(null, "Operación cancelada");
                    return;
                }
            }
        }
    }

    String password = "fin";
    public int intentos = 3;
    public final String usuario;

    public static clsPassword get_user() {
        return new clsPassword(JOptionPane.showInputDialog("Ingrese su usuario"));
    }

    private clsPassword(String usuario) {
        this.usuario = usuario;
    }

    public Result<char[], APIError.OperacionCancelada> read_password() {

        JPasswordField objLeerPassword = new JPasswordField();
        if (JOptionPane.showConfirmDialog(null, new Object[] { this.usuario, "Ingrese su contraseña", objLeerPassword },
                "Inicio de sesión", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.CANCEL_OPTION) {
            return Result.error(new APIError.OperacionCancelada());
        }
        return Result.ok(objLeerPassword.getPassword());
    }

    public Result<String, APIError> login(char[] password) {
        if (intentos == 0) {
            return Result.error(new APIError.IntentoAgotado());
        }
        if (!matches_password(password)) {
            intentos--;
            if (intentos == 0) {
                return Result.error(new APIError.IntentoAgotado());
            }
            return Result.error(new APIError.ContraseñaIncorrecta());
        }
        return Result.ok("Ingreso exitoso");
    }

    boolean matches_password(char[] password) {
        boolean ret = (this.password.equals(new String(password)));
        // Zeroize password
        for (int i = 0; i < password.length; i++) {
            password[i] = 0;
        }

        return ret;
    }

    public int intentos() {
        return this.intentos;
    }
}

package com.utp.clsHerramientas.pry4;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Optional;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

import com.utp.utils.Result;

public class Opciones {
    public static int menu() {
        return JOptionPane.showOptionDialog(null,
                "Elija una opción",
                "Menu de opciones", 0, JOptionPane.QUESTION_MESSAGE, null,
                new Object[] { "Pendiente de dos puntos", "Punto Medio", "Raíces de una función" }, null);
    }

    /**
     * <p>
     * Calcular la pendiente de una recta a partir de dos puntos.
     * </p>
     * <ul>
     * <li>
     * Formula m= (y2-y1)/(x2-x1)
     * </li>
     * <li>
     * Donde (X1, Y1) y (X2 y Y2) son las coordenadas de los dos puntos (límites del
     * segmento) en el plano cartesiano, los cuales deben ser leídos desde el
     * teclado.
     * </li>
     * </ul>
     */
    public static BigDecimal Opcion1_pendiente() {
        Pos2[] puntos = { null, null };
        int index = 0;
        while (index < 2) {
            // Popular puntos
            if (puntos[index] == null) {
                String user_input = JOptionPane.showInputDialog("Ingrese el punto " + (index + 1) + " (x, y)");
                Result<Pos2, ApiError.EntradaInvalida> input = Pos2.fromString(user_input);
                if (input.isError()) {
                    JOptionPane.showMessageDialog(null, input.unwrapError().get_message());
                    continue;
                }
                puntos[index] = input.unwrapOk();
                index++;
            }
        }
        return puntos[0].pendiente_con(puntos[1]);
    }

    /**
     * Calcular el punto medio de una recta. Donde las coordenadas (x,y) de los
     * puntos A y B (límites del segmento) deben ser leídas desde el teclado.
     * 
     * <p>
     * Formula para calcular las coordenadas en el eje X y Y del punto medio:
     * </p>
     * <ul>
     * <li>Punto medio en x = (x1+x2)/2</li>
     * <li>Punto medio en y = (y1+y2)/2</li>
     * </ul>
     */
    public static Pos2 Opcion2_punto_medio() {
        Pos2[] puntos = { null, null };
        int index = 0;
        while (index < 2) {
            // Popular puntos
            if (puntos[index] == null) {
                String user_input = JOptionPane.showInputDialog("Ingrese el punto " + (index + 1) + " (x, y)");
                Result<Pos2, ApiError.EntradaInvalida> input = Pos2.fromString(user_input);
                if (input.isError()) {
                    JOptionPane.showMessageDialog(null, input.unwrapError().get_message());
                    continue;
                }
                puntos[index] = input.unwrapOk();
                index++;
            }
        }
        return puntos[0].punto_medio_con(puntos[1]);
    }

    /**
     * Calcular las raíces de una ecuación cuadrática.
     * <p>
     * Formula: x=(-b±√(b<sup>2</sup>-4ac))/2a
     * </p>
     */
    public static Optional<BigDecimal[]> Opcion3_raiz_cuadratica() {
        BigDecimal[] coeficientes = { null, null, null };
        // Leer hasta obtener los datos
        for (; true;) {
            var result = leer_coeficientes(coeficientes);
            if (result.isError()) {
                JOptionPane.showMessageDialog(null, result.unwrapError().get_message() + "\nIntente de nuevo");
                continue;
            }
            break;
        }
        // Calcular partes de la fórmula
        BigDecimal a = coeficientes[0];
        BigDecimal b = coeficientes[1];
        BigDecimal c = coeficientes[2];
        BigDecimal discriminante = b.pow(2).subtract(a.multiply(c).multiply(new BigDecimal(4)));
        if (discriminante.compareTo(BigDecimal.ZERO) < 0) {
            JOptionPane.showMessageDialog(null, "La ecuación no tiene raíces reales");
            return Optional.empty();
        }
        BigDecimal raiz_discriminante = discriminante.sqrt(Pos2.MC);
        BigDecimal denominador = a.multiply(new BigDecimal(2));

        // Calcular raíces
        BigDecimal raiz1 = b.negate().add(raiz_discriminante).divide(denominador, Pos2.MC);
        BigDecimal raiz2 = b.negate().subtract(raiz_discriminante).divide(denominador, Pos2.MC);
        return Optional.of(new BigDecimal[] { raiz1, raiz2 });
    }

    public static Result<String, ApiError<String>> leer_coeficientes(BigDecimal[] coeficientes) {
        JTextField[] fields = new JTextField[] { new JTextField(), new JTextField(), new JTextField() };
        var a = fields[0];
        var b = fields[1];
        var c = fields[2];
        ArrayList<Object> elementos = new ArrayList<>();
        elementos.add("Ingrese el coeficiente de a");
        elementos.add(a);
        elementos.add("Ingrese el coeficiente de b");
        elementos.add(b);
        elementos.add("Ingrese el coeficiente de c");
        elementos.add(c);
        var result = JOptionPane.showConfirmDialog(null, elementos.toArray(), "Coeficientes de la fórmula",
                JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.CANCEL_OPTION)
            return Result.error(new ApiError.OperacionCancelada<>());

        for (int i = 0; i < 3; i++) {
            String input = fields[i].getText();
            try {
                coeficientes[i] = new BigDecimal(input, Pos2.MC);
            } catch (NumberFormatException e) {
                return Result
                        .error(new ApiError.EntradaInvalida(input, "La entrada no puede ser convertida a un número"));
            }
        }
        return Result.ok("Ok...");
    }

    public static Result<BigDecimal, ApiError.EntradaInvalida> readBigDecimal(String input) {
        String user_input = JOptionPane.showInputDialog(input);
        try {
            return Result.ok(new BigDecimal(user_input, Pos2.MC));
        } catch (NumberFormatException e) {
            return Result
                    .error(new ApiError.EntradaInvalida(user_input, "La entrada no puede ser convertida a un número"));
        }
    }
}

record Pos2(BigDecimal x, BigDecimal y) {
    public BigDecimal pendiente_con(Pos2 rhs) {
        return y.subtract(rhs.y).divide(x.subtract(rhs.x));
    }

    public Pos2 punto_medio_con(Pos2 rhs) {
        BigDecimal half_x = x.add(rhs.x).divide(new BigDecimal(2));
        BigDecimal half_y = y.add(rhs.y).divide(new BigDecimal(2));
        return new Pos2(half_x, half_y);
    }

    final static MathContext MC = new MathContext(32, RoundingMode.HALF_EVEN);;

    public static Result<Pos2, ApiError.EntradaInvalida> fromString(String input) {
        String[] parts = input.split(",");
        if (parts.length != 2) {
            return Result.error(new ApiError.EntradaInvalida(input, "Se esperaban dos números separados por coma"));
        }

        try {
            return Result.ok(new Pos2(new BigDecimal(parts[0], MC), new BigDecimal(parts[1], MC)));
        } catch (NumberFormatException e) {
            return Result.error(new ApiError.EntradaInvalida(input, "La entrada no puede ser convertida a un número"));
        }
    }
}
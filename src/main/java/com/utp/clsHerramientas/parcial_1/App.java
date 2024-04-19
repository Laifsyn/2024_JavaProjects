package com.utp.clsHerramientas.pry2;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.utp.utils.Result;

public class App {
    public static Optional<String> readLine() {
        return Optional.ofNullable(System.console().readLine());
    }

    static Result<BigDecimal, String> readBigDecimal(String message) {
        System.out.print(message);
        Optional<String> line = readLine();
        if (line.isEmpty()) {
            return Result.error("No se ingresó un valor.");
        }
        try {
            return Result.ok(new BigDecimal(line.get()));
        } catch (Exception e) {
            return Result.error("El valor ingresado no es un número : `{}`%n".replace("{}", line.get()));
        }
    }

    public static void main(String[] args) {
        while (true) {
            System.out.println("1-) Calcular el interes compuesto.");
            System.out.println("2-) Calcular la velocidad de los corredores.");
            System.out.println("3-) Salir.");
            var opcion = System.console().readLine();
            switch (opcion) {
                case "1" -> {
                    while (true) {
                        // Leer los valores de entrada
                        Result<BigDecimal, String> capital = readBigDecimal("Ingrese el capital a depositar: ");
                        if (capital.isError()) {
                            System.out.println(capital.unwrapError());
                            continue;
                        }
                        Result<BigDecimal, String> tasa = readBigDecimal("Ingrese la tasa de interés: ");
                        if (tasa.isError()) {
                            System.out.println(tasa.unwrapError());
                            continue;
                        }
                        Result<Integer, String> semanas = readBigDecimal(
                                "Ingrese la duración del depósito en semanas: ")
                                        .flatMap(bd -> Result.ok(bd.intValue()));
                        if (semanas.isError()) {
                            System.out.println(semanas.unwrapError());
                            continue;
                        }
                        // Calcular el interes compuesto
                        BigDecimal result = InteresCompositivo.calcularInteres(capital.get(), tasa.get(),
                                semanas.get());
                        System.out.printf(
                                "El capital total acumulado al final del período de `%d` semanas para un capital de `%s` es: `%s` %n%n",
                                semanas.get(), capital.get().setScale(2, RoundingMode.HALF_EVEN).toPlainString(),
                                result.setScale(2, RoundingMode.HALF_EVEN));
                        break;
                    }
                }
                case "2" -> {
                    var tiempoCarrera = new TiempoCarrera();
                    while (true) {
                        System.out.println(
                                "Ingrese el tiempo de los corredores en el formato`(minutos, segundos)... Ingrese (0,0) para salir`: ");
                        String entrada = System.console().readLine().trim();
                        // Verificar si la entrada de dato esta en el formato adecuado
                        if (!entrada.matches("\\(.*\\)")) {
                            System.out.printf("`%s` no esá en un formato reconocible.%n", entrada);
                            continue;
                        }
                        // Extraer los valores de la entrada
                        String[] entradas = entrada.substring(1, entrada.length() - 1).split(",");
                        // Revisar que se hayan ingresado la cantidad correcta de argumentos
                        if (entradas.length < 2) {
                            System.out.printf("Argumentos incompletos en `%s`%n", entrada);
                            continue;
                        }

                        if (entradas[0].equals("0") && entradas[1].equals("0")) {
                            System.out.println("Obteniendo datos....%n");
                            break;
                        }
                        // Intentar convertir los valores a enteros
                        List<Result<Integer, String>> resultados = Arrays.asList(tryParseInt(entradas[0]),
                                tryParseInt(entradas[1]));
                        // Verificar si hay errores en los datos
                        StringBuilder errors = new StringBuilder();
                        for (var result : resultados) {
                            if (result.isError()) {
                                errors.append(result.unwrapError() + "%n");
                            }
                        }
                        if (!errors.isEmpty()) {
                            System.out.println(errors.toString());
                            continue;
                        }
                        // Obtener los datos en entero
                        var minuto = resultados.get(0).unwrapOk();
                        var segundo = resultados.get(1).unwrapOk();
                        // Verificar si se tiene que salir
                        if (minuto == 0 && segundo == 0) {
                            break;
                        }
                        Result<Object, String> result = tiempoCarrera.insertarTiempo(new int[] { minuto, segundo });
                        if (result.isError()) {
                            System.out.println(result.unwrapError());
                        }
                    }
                    tiempoCarrera.imprimirVelocidad();
                }
                case "3" -> {
                    return;
                }
                default -> System.out.println("Opción no válida.");
            }
        }
    }

    static Result<Integer, String> tryParseInt(String input) {
        try {
            return Result.ok(Integer.parseInt(input));
        } catch (Exception e) {
            return Result.error(String.format("`%s` is not a valid format", input));
        }
    }

}

// • Muchos bancos y cajas de ahorro calculan los intereses de las cantidades
// depositadas por los clientes diariamente según las premisas siguientes. Un
// capital de 1,000 dólares, con una tasa de interés del 6 por 100, renta un
// interés en un día de 0.06 multiplicado por 1,000 y dividido por 365. Esta
// operación producirá 0,16 dólares de interés y el capital acumulado será
// 1,000.16. El interés para el segundo día se calculará multiplicando 0.06 por
// 1,000 y dividiendo el resultado por 365. Diseñar un módulo que reciba tres
// entradas: el capital a depositar, la tasa de interés y la duración del
// depósito en semanas, y calcular el capital total acumulado al final del
// período de tiempo especificado.
class InteresCompositivo {
    public static final int SCALE = 64;

    private InteresCompositivo() {
    }

    public static BigDecimal calcularInteres(BigDecimal capital, BigDecimal tasa, int semanas) {
        // Interes Calculado => Capital * ( 1.0 + Tasa/(365*100) ) ^ (semanas * 7)
        var en_plazo_de = BigDecimal.valueOf(365);
        var tasa_decimal = tasa.divide(BigDecimal.valueOf(100).multiply(en_plazo_de), SCALE,
                RoundingMode.HALF_EVEN);
        BigDecimal interes = BigDecimal.ONE.add(tasa_decimal);
        return capital.multiply(interes.pow(semanas * 7));
    }
}

// • Diseñar un módulo para calcular la velocidad (en m/s) de los corredores de
// la carrera de 1,500 metros. La entrada consistirá en parejas de números
// (minutos, segundos) que dan el tiempo del corredor; por cada corredor, el
// algoritmo debe visualizar el tiempo en minutos y segundos, así como la
// velocidad media. Ejemplo de entrada de datos: (3,53) (3,40) (3,46) (3,52)
// (4,0) (0,0); el último par de datos se utilizará como fin de entrada de
// datos.
class TiempoCarrera {
    ArrayList<Duration> tiempos = new ArrayList<>();
    static final double DISTANCIA = 1500.0;

    public Result<Object, String> insertarTiempo(int[] tiempo) {
        if (tiempo.length != 2) {
            return Result.error("El tiempo debe tener dos valores.");
        }
        if (tiempo[0] < 0 || tiempo[1] < 0) {
            return Result.error("El tiempo no puede tener componentes negativo.");
        }
        if (tiempo[1] >= 60) {
            return Result.error("Los segundos no pueden ser mayores a 59.");
        }
        var time = Duration.ofSeconds(tiempo[0] * 60L + tiempo[1]);
        // Check value isn't higher than 120 minutes
        if (time.toSeconds() > 120 * 60) {
            return Result.error("Los competidores no pueden correr más de 120 minutos.");
        }
        tiempos.add(time);
        return Result.ok(null);
    }

    public void imprimirVelocidad() {
        Integer contador = 1;
        if (tiempos.isEmpty()) {
            System.out.println("No se ingresaron tiempos.");
            return;
        }
        for (var tiempo : tiempos) {
            BigDecimal velocidad = BigDecimal.valueOf(DISTANCIA / tiempo.getSeconds()).setScale(2,
                    RoundingMode.HALF_EVEN);
            System.out.printf("%s-)Velocidad: %s m/s (tiempo: %s)%n", contador, velocidad, tiempo.toString());
            contador++;
        }
    }
}
package com.utp.clsHerramientas.parcial_1;

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
        if (line.get().equals("exit")) {
            System.exit(0);
        }
        try {
            return Result.ok(new BigDecimal(line.get()));
        } catch (Exception e) {
            return Result.error("El valor ingresado no es un número : `{}`".replace("{}", line.get()));
        }
    }

    public static void main(String[] args) {
        while (true) {
            System.out.println("1-) Calcular el interes compuesto.");
            System.out.println("2-) Calcular la velocidad de los corredores.");
            System.out.println("3-) Reporte climático.");
            System.out.println("4-) Salir.");
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
                    while (true) {
                        System.out.println("Ingrese los datos iniciales: ");
                        Result<BigDecimal, String> maybe_temperatura = readBigDecimal("Temperatura: ");
                        if (maybe_temperatura.isError()) {
                            System.out.println(maybe_temperatura.unwrapError());
                            continue;
                        }
                        var maybe_probabilidadLluvia = readBigDecimal("Probabilidad de lluvia: ");
                        if (maybe_probabilidadLluvia.isError()) {
                            System.out.println(maybe_probabilidadLluvia.unwrapError());
                            continue;
                        }
                        var maybe_dias = readBigDecimal("Días a predecir: ");
                        if (maybe_dias.isError()) {
                            System.out.println(maybe_dias.unwrapError());
                            continue;
                        }
                        double temperatura = maybe_temperatura.get().doubleValue();
                        int dias = maybe_dias.get().intValue();
                        if (dias < 0) {
                            System.out.println("El número de días no puede ser negativo.");
                            continue;
                        }
                        double probabilidadLluvia = maybe_probabilidadLluvia.get().doubleValue();

                        ProbabilidadLluvia pronostico = new ProbabilidadLluvia(temperatura, probabilidadLluvia, dias);
                        ArrayList<ReporteNDia> reportes = new ArrayList<>();
                        for (int i = 0; i < dias; i++) {
                            System.out.println("Ingrese la temperatura del día " + (i + 1) + ": ");
                            maybe_temperatura = readBigDecimal("Temperatura: ");
                            if (maybe_temperatura.isError()) {
                                System.out.println(maybe_temperatura.unwrapError());
                                continue;
                            }
                            double temperatura_hoy = maybe_temperatura.get().doubleValue();
                            Optional<ReporteNDia> reporte = pronostico.next_with_temperatura(temperatura_hoy);
                            if (reporte.isEmpty()) {
                                break;
                            }
                            reportes.add(reporte.get());
                        }
                        if (reportes.isEmpty()) {
                            System.out.println("No se ingresaron reportes.");
                            continue;
                        }
                        double max_temp = reportes.get(0).temperatura();
                        double min_temp = max_temp;
                        int dias_con_lluvia = 0;
                        for (var reporte : reportes) {
                            if (reporte.temperatura() > max_temp) {
                                max_temp = reporte.temperatura();
                            }
                            if (reporte.temperatura() < min_temp) {
                                min_temp = reporte.temperatura();
                            }
                            if (reporte.probabilidadLluvia() >= 100.0) {
                                dias_con_lluvia++;
                            }
                            System.out.println(reporte.leer_reporte());
                        }
                        System.out.printf("%nPronóstico para los próximos %d días:%n", dias);
                        if (dias_con_lluvia == reportes.size()) {
                            System.out.println("Va a llover todos los días.");
                        } else {
                            System.out.printf("Va a llover %d de %d dias%n", dias_con_lluvia, reportes.size());
                        }
                        // Rango de temperaturas
                        System.out.printf("Temperatura máxima: %.2f%n", max_temp);
                        System.out.printf("Temperatura mínima: %.2f%n", min_temp);
                        break;
                    }
                }
                case "4" -> {
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

record ReporteNDia(int dia, double temperatura, double probabilidadLluvia) {
    public String leer_reporte() {
        String llovio = probabilidadLluvia >= 100.0 ? "Llovió" : "No llovió";
        return String.format("Dia: `%d`, Temperatura: `%.1f centigrados`, %s (%.1f%% lluvia)", dia, temperatura,
                llovio, probabilidadLluvia);
    }
}

/*
 * Adicione un módulo que simule las condiciones climáticas (temperatura y
 * probabilidad de lluvia) de la ciudad de David al pasar un número concreto de
 * días según estas reglas:
 * o La temperatura inicial y el % de probabilidad lluvia lo define el usuario.
 * o Cada día que pasa:
 *  10% de posibilidades de lluvias se da cuando la temperatura aumente o
 * disminuye 2 grados.
 *  Si la temperatura supera los 25 grados, la probabilidad de lluvia al día
 * siguiente aumenta un 20%,
 *  Si la temperatura baja 5 grados, la probabilidad de lluvia al día siguiente
 * en un disminuye 20%.
 *  Si llueve (100%), la temperatura del día siguiente disminuye en 1 grado
 * o El método recibe en número de días de la predicción y muestra la
 * temperatura y si llueve durante todo esos días.
 * o También mostrará la temperatura máxima y mínima de ese periodo y cuantos
 * días va a llover
 */
class ProbabilidadLluvia {
    private double temperatura;
    private double probabilidadLluvia;
    final private int dias;
    private int dia = 0;

    public ProbabilidadLluvia(double temperatura, double probabilidadLluvia, int dias) {
        this.update_temperatura(temperatura);
        this.update_probabilidad_lluvia(probabilidadLluvia);
        this.dias = dias;
    }

    void update_temperatura(double nueva_temperatura) {
        offset_temperatura(nueva_temperatura - temperatura);
    }

    void offset_temperatura(double offset) {
        // redondear a 0 cifras
        offset = Math.round(offset);
        if (Math.abs(offset) == 2.0) {
            this.update_probabilidad_lluvia(probabilidadLluvia + 10);
        }
        if (offset == -5.0) {
            this.update_probabilidad_lluvia(probabilidadLluvia - 10);
        }
        this.temperatura += offset;

    }

    void update_probabilidad_lluvia(double probabilidad_lluvia) {
        this.probabilidadLluvia = probabilidad_lluvia;
        if (this.probabilidadLluvia > 100.0) {
            this.probabilidadLluvia = 100.0;
        }
    }

    // Computa el reporte de hoy
    public Optional<ReporteNDia> next_with_temperatura(final double temperatura_hoy) {
        if (this.dia == dias) {
            return Optional.empty();
        }
        double temperatura_offset = 0;
        double temperatura_ayer = this.temperatura;
        double probabilidad_lluvia_ayer = this.probabilidadLluvia;
        // si la temperatura de hoy es mayor a 25 grados, aumentar la probabilidad de
        // lluvia
        if (temperatura_ayer > 25.0) {
            this.update_probabilidad_lluvia(this.probabilidadLluvia + 20);
        }
        // Si ayer llueve, hoy la temperatura disminuye en 1 grado
        if (probabilidad_lluvia_ayer >= 100.0) {
            temperatura_offset -= 1.0;
        }
        this.update_temperatura(temperatura_hoy + temperatura_offset);
        this.dia += 1;
        return Optional.of(new ReporteNDia(this.dia, this.temperatura, this.probabilidadLluvia));
    }

} // ProbabilidadLluvia
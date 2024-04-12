package com.utp.clsHerramientas.pry2;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;

import javax.lang.model.type.NullType;

import com.utp.utils.Result;

public class App {

    // Escriba un programa en java que permita poner en funcionamiento los
    // siguientes módulos:
    //
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
    public static void main(String[] args) {
        System.out.println("Hello World!");
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
    ArrayList<Duration> tiempos = new ArrayList<Duration>();
    static final int DISTANCIA = 1500;

    public Result<NullType, String> insertarTiempo(int[] tiempo) {
        if (tiempo.length != 2) {
            return Result.error("El tiempo debe tener dos valores.");
        }
        if (tiempo[0] < 0 || tiempo[1] < 0) {
            return Result.error("El tiempo no puede tener componentes negativo.");
        }
        if (tiempo[1] >= 60) {
            return Result.error("Los segundos no pueden ser mayores a 59.");
        }
        var time = Duration.ofSeconds(tiempo[0] * 60 + tiempo[1]);
        // Check value isn't higher than 120 minutes
        if (time.toSeconds() > 120 * 60) {
            return Result.error("Los competidores no pueden correr más de 120 minutos.");
        }
        tiempos.add(time);
        return Result.ok(null);
    }

    public void imprimirVelocidad() {
        Integer contador = 1;
        for (var tiempo : tiempos) {
            BigDecimal velocidad = BigDecimal.valueOf(DISTANCIA / tiempo.getSeconds());
            System.out.printf("%s-)Velocidad: %s m/s", contador, velocidad);
        }
    }
}
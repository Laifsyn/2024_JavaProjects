package com.utp.clsEstructuraDiscretas.pry4;

import java.awt.Color;
import java.math.BigInteger;

import com.utp.utils.Result;

public enum VarianteFormula {
    PERMUTACIONES, COMBINACIONES, VARIANZA, PERMUTACIONES_REPETIDAS, COMBINACIONES_REPETIDAS, VARIANZA_REPETIDAS;

    static final Color Azul = new Color(1, 75, 160);
    static final Color Morado = new Color(163, 73, 164);
    static final Color Dorado = new Color(239, 184, 16);
    static final Color Chocolate = new Color(105, 63, 38);
    static final Color AzulClaro = new Color(0, 171, 240);
    static final Color Magenta = new Color(200, 0, 167);

    public Color as_color() {
        switch (this) {
            case PERMUTACIONES -> {
                return Azul;
            }
            case COMBINACIONES -> {
                return Morado;
            }
            case VARIANZA -> {
                return Dorado;
            }
            case PERMUTACIONES_REPETIDAS -> {
                return Chocolate;
            }
            case COMBINACIONES_REPETIDAS -> {
                return AzulClaro;
            }
            case VARIANZA_REPETIDAS -> {
                return Magenta;
            }
            default -> {
                return Color.BLACK;
            }
        }

    }

    public Result<BigInteger, Exception> calcular(String n, String r) {
        switch (this) {
            case PERMUTACIONES -> {
                return Formulas.Permutacion_sin_repeticion(new String[] { n, r });
            }
            case COMBINACIONES -> {
                return Formulas.Comb_sin_repeticion(new String[] { n, r });
            }
            case VARIANZA -> {
                return Formulas.Varianza_sin_repeticion(new String[] { n, r });
            }
            case PERMUTACIONES_REPETIDAS -> {
                var split_r = r.split(",");
                for (int i = 0; i < split_r.length; i++)
                    split_r[i] = split_r[i].trim();
                String[] args = new String[split_r.length + 1];
                args[0] = n;
                System.arraycopy(split_r, 0, args, 1, split_r.length);
                return Formulas.Permutacion_con_repeticion(args);
            }
            case COMBINACIONES_REPETIDAS -> {
                return Formulas.Comb_con_repeticion(new String[] { n, r });
            }
            case VARIANZA_REPETIDAS -> {
                return Formulas.Varianza_con_repeticion(new String[] { n, r });
            }
        }
        return Result.error(new UnsupportedOperationException("No se ha implementado la formula"));
    }
}

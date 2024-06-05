package com.utp.clsEstructuraDiscretas.pry4;

import java.math.BigInteger;

import com.utp.utils.Result;

public class Formulas {

    private static BigInteger factorial(int n) {
        if (n == 0) {
            return BigInteger.ONE;
        }
        BigInteger result = BigInteger.ONE;
        for (int i = 1; i <= n; i++) {
            result = result.multiply(BigInteger.valueOf(i));
        }
        return result;
    }
    public static Result<BigInteger, Exception> Comb_sin_repeticion(String[] args) {
        var this_function = new Object() {}.getClass().getEnclosingMethod().getName();
        try {
            int n = Integer.parseInt(args[0]);
            int r = Integer.parseInt(args[1]);
            return Result.ok(factorial(n).divide(factorial(r).multiply(factorial(n - r))));
        } catch (Exception e) {
            return Result.error(e);
        }
    }

    public static Result<BigInteger, Exception> Comb_con_repeticion(String[] args) {
        var this_function = new Object() {}.getClass().getEnclosingMethod().getName();
        try {
            int n = Integer.parseInt(args[0]);
            int r = Integer.parseInt(args[1]);
            return Result.ok(factorial(n + r - 1).divide(factorial(r).multiply(factorial(n - 1))));
        } catch (Exception e) {
            return Result.error(e);
        }
    }

    public static Result<BigInteger, Exception> Varianza_sin_repeticion(String[] args) {
        var this_function = new Object() {}.getClass().getEnclosingMethod().getName();
        try {
            int n = Integer.parseInt(args[0]);
            int r = Integer.parseInt(args[1]);
            return Result.ok(factorial(n).divide(factorial(n - r)));
        } catch (Exception e) {
            return Result.error(e);
        }
    }

    public static Result<BigInteger, Exception> Varianza_con_repeticion(String[] args) {
        var this_function = new Object() {}.getClass().getEnclosingMethod().getName();
        return Result.error(new UnsupportedOperationException(this_function + " Not supported yet."));
    }

    public static Result<BigInteger, Exception> Permutacion_sin_repeticion(String[] args) {
        var this_function = new Object() {}.getClass().getEnclosingMethod().getName();
        return Result.error(new UnsupportedOperationException(this_function + " Not supported yet."));
    }

    public static Result<BigInteger, Exception> Permutacion_con_repeticion(String[] args) {
        var this_function = new Object() {}.getClass().getEnclosingMethod().getName();
        return Result.error(new UnsupportedOperationException(this_function + " Not supported yet."));
    }

}

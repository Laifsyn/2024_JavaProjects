package com.utp.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;

public class $ {
    final static BigDecimal thousand = BigDecimal.valueOf(1000);

    public static String pretty_number(BigDecimal number) {
        var fraction = number.remainder(BigDecimal.ONE);
        var integer = number.subtract(fraction);
        var triplets = new ArrayList<Integer>();
        var is_negative = (integer.compareTo(BigDecimal.ZERO) < 0);
        while (integer.compareTo(BigDecimal.ZERO) > 0) {
            var triplet = integer.remainder(thousand).intValue();
            // Inserts in LowEndian
            triplets.add(triplet);
            integer = integer.divide(BigDecimal.valueOf(1000));
            var reimainder = integer.remainder(BigDecimal.ONE);
            integer = integer.subtract(reimainder);
        }

        // Because Triplets were inserted in low endian, we need to reverse them
        Collections.reverse(triplets);
        StringBuilder builder = new StringBuilder();
        for (int i : triplets) {
            builder.append(String.format("%03d,", i));
        }

        // trim ending comma
        builder.deleteCharAt(builder.length() - 1);
        // trim leading zeros until possibly lenght 1
        while (builder.charAt(0) == '0' && builder.length() > 1) {
            builder.deleteCharAt(0);
        }

        if (builder.length() == 0) {
            builder.append("0");
        }
        var is_non_zero_fraction = (fraction.abs().compareTo(BigDecimal.ZERO) > 1);
        if (is_non_zero_fraction) {
            var fraction_str = fraction.toString().substring(1);
            builder.append(fraction_str);
        }
        if (is_negative) {
            builder.insert(0, "-");
        }
        return builder.toString();
    }

    static public Integer[] into_triplets(Integer num) {
        var vec = new ArrayList<Integer>();
        while (num > 0) {
            vec.add(num % 1000);
            num /= 1000;
        }
        Collections.reverse(vec);
        // System.out.println(vec.toString());
        return vec.toArray(new Integer[0]);
    }

    static public void pretty_print_int(Integer num) {
        var triplets = $.into_triplets(num);
        for (int i = 0; i < triplets.length; i++) {
            if (i == 0) {
                System.out.print(triplets[i]);
            } else {
                System.out.printf("%03d", triplets[i]);
            }
            if (i < triplets.length - 1) {
                System.out.print(",");
            }
        }
    }
}

package com.utp.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;

public class $ {
    public static String pretty_number(BigDecimal number) {
        var fraction = number.remainder(BigDecimal.ONE);
        var integer = number.subtract(fraction);
        var triplets = new ArrayList<Integer>();
        while (integer.compareTo(BigDecimal.ZERO) ) {
            var triplet = integer % 1000;
            triplets.add(triplet);
            integer /= 1000;
            integer = integer.remainder(BigDecimal.ONE);
        }
        return String.format("%,d%s", integer.intValue(), fraction);
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

package com.utp.clsEstructuraDatos.pry1.app;

import java.util.ArrayList;
import java.util.Collections;

public class Utils {
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
        var triplets = Utils.into_triplets(num);
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
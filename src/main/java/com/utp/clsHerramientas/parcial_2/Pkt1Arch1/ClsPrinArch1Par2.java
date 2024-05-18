package com.utp.clsHerramientas.parcial_2.Pkt1Arch1;

import java.math.BigInteger;
import java.util.Optional;

import com.utp.clsHerramientas.parcial_2.ThreeNumbers;
import static java.math.BigInteger.valueOf;

public class ClsPrinArch1Par2 {
    public static void main(String[] args) {

    }

    BigInteger MtdExpresion1Par2(ThreeNumbers threeNumbers) {

        final BigInteger const_a = valueOf(3).divide(valueOf(5));
        final BigInteger const_b = valueOf(1).divide(valueOf(2));
        final BigInteger const_c = valueOf(1);

        var num1 = threeNumbers.num1();
        var num2 = threeNumbers.num2();
        var num3 = threeNumbers.num3();
        return (const_a.multiply(num1)).subtract(const_b.multiply(num2)).add(const_c.multiply(num3));
    }
}

class ClsPrinRestaPar2 {
    BigInteger MtdExpresion2Par2(ThreeNumbers threeNumbers) {
        final BigInteger const_a = valueOf(1).divide(valueOf(3));
        final BigInteger const_b = valueOf(1).divide(valueOf(2));
        final BigInteger const_c = valueOf(1).divide(valueOf(6));

        var num1 = threeNumbers.num1().pow(2);
        var num2 = threeNumbers.num2().pow(2);
        var num3 = threeNumbers.num3().pow(2);
        return (const_a.multiply(num1)).subtract(const_b.multiply(num2)).add(const_c.multiply(num3));
    }
}


// class ClsSecArch1Par2{
//     Optional<BigInteger> MtdDivision(ThreeNumbers threeNumbers) {
//         var num1 = threeNumbers.num1();
//         var num2 = threeNumbers.num2();
//         var num3 = threeNumbers.num3();
//         if (num3.equals(BigInteger.ZERO)) {
//             return Optional.empty();
//         }
//         return Optional.of(num1.multiply(num2).divide(num3));
//     }
// }
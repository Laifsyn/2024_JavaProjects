package com.utp.clsHerramientas.parcial_2.PktArch2;

import java.math.BigInteger;

import com.utp.clsHerramientas.parcial_2.ThreeNumbers;

class ClsMultArch1Par2 {
    BigInteger MtdMultPar2(ThreeNumbers threeNumbers) {
        var num1 = threeNumbers.num1();
        var num2 = threeNumbers.num2();
        var num3 = threeNumbers.num3();
        return num1.multiply(num2).multiply(num3);
    }
}
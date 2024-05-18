package com.utp.clsHerramientas.parcial_2.Pkt1Arch1;

import java.math.BigInteger;
import java.util.Optional;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

import com.utp.clsHerramientas.parcial_2.ThreeNumbers;
import com.utp.utils.Result;

import static java.math.BigInteger.valueOf;

public class ClsPrinArch1Par2 {
    public static void main(String[] args) {
        ThreeNumbers numbers = App.leer_numeros();
        Object[] resultados = new Object[4];
        BigInteger expresion_1 = ClsPrinSumaPar2.MtdExpresion1Par2(numbers);
        resultados[0] = String.format("El resultado de la primera expresión es: %s", expresion_1);
        BigInteger expresion_2 = ClsPrinRestaPar2.MtdExpresion2Par2(numbers);
        resultados[1] = String.format("El resultado de la segunda expresión es: %s", expresion_2);
        // BigInteger multiplicacion = ClsSecArch1Par2.MtdDivision(numbers);
        // resultados[2] = String.format("El resultado de la multiplicación es: %s",
        // multiplicacion);
        Optional<BigInteger> division = ClsSecArch1Par2.MtdDivision(numbers);
        resultados[2] = division.map(value -> String.format("El resultado de la división es: %s", value))
                .orElse("No se puede dividir por cero");
        JOptionPane.showMessageDialog(null, resultados, "Resultados", JOptionPane.INFORMATION_MESSAGE);
    }

    static BigInteger MtdExpresion1Par2(ThreeNumbers threeNumbers) {

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
    static BigInteger MtdExpresion2Par2(ThreeNumbers threeNumbers) {
        final BigInteger const_a = valueOf(1).divide(valueOf(3));
        final BigInteger const_b = valueOf(1).divide(valueOf(2));
        final BigInteger const_c = valueOf(1).divide(valueOf(6));

        var num1 = threeNumbers.num1().pow(2);
        var num2 = threeNumbers.num2().pow(2);
        var num3 = threeNumbers.num3().pow(2);
        return (const_a.multiply(num1)).subtract(const_b.multiply(num2)).add(const_c.multiply(num3));
    }
}

class App {

    static ThreeNumbers leer_numeros() {
        JTextField[] entradas = new JTextField[] { new JTextField(), new JTextField(), new JTextField() };
        Object[] elementos_de_entrada = new Object[] { "Ingrese el primer número", entradas[0],
                "Ingrese el segundo número",
                entradas[1], "Ingrese el tercer número", entradas[2] };
        int confirm_dialog_result = JOptionPane.showConfirmDialog(null, entradas, "Ingrese los números");
        BigIntger[] numeros = new BigInteger[3];

        for (int i = 0; i < 3; i++) {
            try {
                numeros[i] = new BigInteger(entradas[i].getText());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null,
                        String.format("%s\nNo puede ser convertido a entero: `%s`", e.getMessage(), entradas[1]),
                        "Error de lectura", JOptionPane.ERROR_MESSAGE);
                return leer_numeros();
            }
        }
        return new ThreeNumbers(numeros[0], numeros[1], numeros[2]);
    }
}

// class ClsSecArch1Par2{
// Optional<BigInteger> MtdDivision(ThreeNumbers threeNumbers) {
// var num1 = threeNumbers.num1();
// var num2 = threeNumbers.num2();
// var num3 = threeNumbers.num3();
// if (num3.equals(BigInteger.ZERO)) {
// return Optional.empty();
// }
// return Optional.of(num1.multiply(num2).divide(num3));
// }
// }
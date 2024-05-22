// package com.utp.clsHerramientas.parcial_2.Pkt1Arch1;

// import java.math.BigDecimal;
// import java.math.RoundingMode;
// import java.util.Optional;

// import javax.swing.JOptionPane;
// import javax.swing.JTextField;

// import com.utp.clsHerramientas.parcial_2.PktArch2.ClsMultArch1Par2;
// import com.utp.parcial_2.ThreeNumbers;
// import com.utp.parcial_2.PktSecPar21.ClsSecArch1Par2;

// import static java.math.BigDecimal.valueOf;

// public class ClsPrinArch1Par2 {
//     public static void main(String[] args) {
//         ThreeNumbers numbers = App.leer_numeros();
//         Object[] resultados = new Object[4];
//         BigDecimal expresion_1 = ClsPrinArch1Par2.MtdExpresion1Par2(numbers);
//         resultados[0] = String.format("El resultado de la primera expresión es: %s", expresion_1);

//         BigDecimal expresion_2 = ClsPrinRestaPar2.MtdExpresion2Par2(numbers);
//         resultados[1] = String.format("El resultado de la segunda expresión es: %s", expresion_2);

//         BigDecimal multiplicacion = ClsMultArch1Par2.MtdMultPar2(numbers);
//         resultados[2] = String.format("El resultado de la multiplicación es: %s", multiplicacion);

//         Optional<BigDecimal> division = ClsSecArch1Par2.MtdDivision(numbers);
//         resultados[3] = division.map(value -> String.format("El resultado de la división es: %s", value))
//                 .orElse("No se puede dividir por cero");
//         JOptionPane.showMessageDialog(null, resultados, "Resultados", JOptionPane.INFORMATION_MESSAGE);
//     }

//     static BigDecimal MtdExpresion1Par2(ThreeNumbers threeNumbers) {
//         final BigDecimal const_a = valueOf(3).divide(valueOf(5), 6, RoundingMode.HALF_EVEN);
//         final BigDecimal const_b = valueOf(1).divide(valueOf(2), 6, RoundingMode.HALF_EVEN);
//         final BigDecimal const_c = valueOf(1);

//         var num1 = threeNumbers.num1();
//         var num2 = threeNumbers.num2();
//         var num3 = threeNumbers.num3();
//         return (const_a.multiply(num1)).subtract(const_b.multiply(num2)).add(const_c.multiply(num3));
//     }
// }

// class ClsPrinRestaPar2 {
//     static BigDecimal MtdExpresion2Par2(ThreeNumbers threeNumbers) {
//         final BigDecimal const_a = valueOf(1).divide(valueOf(3), 6, RoundingMode.HALF_EVEN);
//         final BigDecimal const_b = valueOf(1).divide(valueOf(2), 6, RoundingMode.HALF_EVEN);
//         final BigDecimal const_c = valueOf(1).divide(valueOf(6), 6, RoundingMode.HALF_EVEN);

//         var num1 = threeNumbers.num1().pow(2);
//         var num2 = threeNumbers.num2().pow(2);
//         var num3 = threeNumbers.num3().pow(2);
//         return (const_a.multiply(num1)).subtract(const_b.multiply(num2)).add(const_c.multiply(num3));
//     }
// }

// class App {

//     static ThreeNumbers leer_numeros() {
//         JTextField[] entradas = new JTextField[] { new JTextField(), new JTextField(), new JTextField() };
//         Object[] elementos_de_entrada = new Object[] { "Ingrese el primer número", entradas[0],
//                 "Ingrese el segundo número",
//                 entradas[1], "Ingrese el tercer número", entradas[2] };
//         JOptionPane.showConfirmDialog(null, elementos_de_entrada, "Ingrese los números", JOptionPane.YES_NO_OPTION);

//         BigDecimal[] numeros = new BigDecimal[3];

//         for (int i = 0; i < 3; i++) {
//             try {
//                 numeros[i] = new BigDecimal(entradas[i].getText());
//             } catch (NumberFormatException e) {
//                 JOptionPane.showMessageDialog(null,
//                         String.format("Error:`%s`\nNo puede ser convertido a entero: `%s`", e.getMessage(),
//                                 entradas[i].getText()),
//                         "Error de lectura", JOptionPane.ERROR_MESSAGE);
//                 return leer_numeros();
//             }
//         }
//         if (numeros[2].compareTo(BigDecimal.valueOf(0)) == 0) {
//             JOptionPane.showMessageDialog(null, "El tercer número no puede ser cero", "Error de lectura",
//                     JOptionPane.ERROR_MESSAGE);
//             return leer_numeros();
//         }
//         return new ThreeNumbers(numeros[0], numeros[1], numeros[2]);
//     }
// }

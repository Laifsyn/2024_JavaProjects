
package com.utp.clsEstructuraDiscretas.pry4;

import javax.swing.JFrame;
import java.awt.GridBagLayout;

public class AppCombinatoria {

}

class UserInterface {
    String pry4 = "Proyecto 4 - Combinatorias";
    JFrame frame = new JFrame(pry4);

    void run_app() {
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridBagLayout());

        frame.setVisible(true);
    }

    public void SendCommand(Commands command) {
        switch (command) {
            case Commands.CalculateCombinatoria argumentos -> {
            }
            case Commands.UpdateRadios() -> {
            }
        }
    }
}

// @formatter:off
sealed interface Commands {
    public record CalculateCombinatoria(String n, String r) implements Commands {}
    public record UpdateRadios() implements Commands {}
}
// @formatter:on
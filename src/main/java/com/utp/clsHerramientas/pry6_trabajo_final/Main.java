package com.utp.clsHerramientas.pry6_trabajo_final;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;

import com.utp.clsHerramientas.pry6_trabajo_final.ui.Factura;
import com.utp.clsHerramientas.pry6_trabajo_final.ui.Reporte;
import com.utp.clsHerramientas.pry6_trabajo_final.ui.UI;

import static com.utp.clsHerramientas.pry6_trabajo_final.App.CMD.*;

public class Main {
    public static void main(String[] args) {
        App app = new App();
        app.run();
    }
}

/**
 * Bibliografías :
 * 1. https://www.rapidtables.com/web/color/RGB_Color.html
 * 2.
 * https://stackoverflow.com/questions/20165564/calculating-days-between-two-dates-with-java
 * 3.
 * https://stackoverflow.com/questions/2138085/java-date-format-including-additional-characters
 * 4.
 * https://stackoverflow.com/questions/2442599/how-to-set-jframe-to-appear-centered-regardless-of-monitor-resolution
 */

class App {
    public final Cliente[] clientes = Cliente.cargar_clientes();
    public final Factura[] facturas = Factura.cargar_facturas();
    public final JFrame frame = new JFrame("Trabajo Final - Arreglo de Objetos");
    final JButton btn_mostrar_factura = new JButton("Mostrar Factura");
    final JTextField txt_buscar = new JTextField(35);
    boolean last_buscar_result = true;

    void run() {
        frame.add(content_pane());
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
    }

    App() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    JPanel content_pane() {
        // Inicializar estado de la aplicación
        send_command(new Buscar(txt_buscar.getText()));

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(2, 0, 2, 0);
        panel.add(new JLabel("Ingrese el Codigo de Cliente"), gbc);

        gbc.gridy = 1;
        panel.add(txt_buscar, gbc);
        gbc.gridy = 2;
        panel.add(btn_mostrar_factura, gbc);

        JButton btn_mostrar_clientes = new JButton("Mostrar Clientes");
        gbc.gridy = 3;
        panel.add(btn_mostrar_clientes, gbc);

        btn_mostrar_clientes.addActionListener(e -> {
            JTextArea txt_area = new JTextArea();
            txt_area.setEditable(false);
            StringBuilder sb = new StringBuilder();
            for (Cliente cliente : clientes) {
                sb.append(cliente.codigo()).append(" :");
                sb.append(cliente.nombre_completo()).append("\n");
            }
            sb.delete(sb.length() - 1, sb.length());
            txt_area.setText(sb.toString());
            JOptionPane.showMessageDialog(btn_mostrar_clientes, txt_area, "Clientes registrados",
                    JOptionPane.INFORMATION_MESSAGE);
        });
        txt_buscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                send_command(new Buscar(txt_buscar.getText()));
                if (btn_mostrar_factura.isEnabled() && KeyEvent.VK_ENTER == evt.getKeyCode()) {
                    send_command(new MostrarFactura());
                }
                evt.consume();
            }
        });

        btn_mostrar_factura.addActionListener(e -> {
            send_command(new MostrarFactura());
        });

        return panel;
    }

    public void send_command(CMD cmd) {
        switch (cmd) {
            case Buscar(String codigo) -> {
                Cliente cliente = null;
                for (Cliente cliente_n : clientes) {
                    if (cliente_n.codigo().equals(codigo)) {
                        if (!last_buscar_result) {
                            last_buscar_result = true;
                            System.out.println("Cliente existe: `" + cliente_n.codigo() + "`");
                        }
                        cliente = cliente_n;
                        break;
                    }
                }
                if (cliente == null) {
                    btn_mostrar_factura.setEnabled(false);
                    if (last_buscar_result) {
                        System.out.println("Cliente: `" + codigo + "` no existe");
                        last_buscar_result = false;
                    }
                    return;
                }
                btn_mostrar_factura.setEnabled(true);
            }
            case MostrarFactura() -> {
                Cliente Cliente = null;
                for (Cliente cliente_n : clientes) {
                    if (cliente_n.codigo().equals(txt_buscar.getText())) {
                        Cliente = cliente_n;
                        break;
                    }
                }
                // reiniciar la búsqueda
                txt_buscar.setText("");
                btn_mostrar_factura.setEnabled(false);
                Reporte reporte = new Reporte(Cliente, facturas);

                var reporte1 = UI.show(reporte.as_reporte_1());
                var reporte2 = UI.show(reporte.as_reporte_2());
                String titulo = "Reporte de Facturas - Cliente: " + Cliente.nombre_completo();
                reporte1.setTitle(titulo);
                reporte2.setTitle(titulo);
                reporte.add_ok_action(new Runnable() {
                    @Override
                    public void run() {
                        reporte1.dispose();
                        reporte2.dispose();
                    }
                });
            }

        }
    }

    public sealed interface CMD {
        // @formatter:off
        public record Buscar(String codigo) implements CMD{};
        public record MostrarFactura() implements CMD{};
        // @formatter:on
    }

}
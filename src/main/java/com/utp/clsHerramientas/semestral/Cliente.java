package com.utp.clsHerramientas.semestral;

public record Cliente(String codigo, String nombre, String apellido, String direccion, String telefono_1,
        String telefono_2) {

    public static final Integer generales_clientes_len = 20;

    public static Cliente[] cargar_clientes() {
        Cliente[] clientes = new Cliente[generales_clientes_len];
        String[] datosGral = new String[generales_clientes_len];
        Datos.generalesCliente(datosGral);
        for (int i = 0; i < generales_clientes_len; i++) {
            String[] datos = datosGral[i].split(" +");
            clientes[i] = new Cliente(datos[0], datos[1], datos[2], datos[3], datos[4], datos[5]);
        }
        return clientes;
    }

    public String nombre_completo(){
        return nombre + " " + apellido;
    }
    public static void main(String[] args) {
        Cliente[] clientes = cargar_clientes();
        for (int i = 0; i < clientes.length; i++) {
            System.out.println(i + "-)" + clientes[i]);
        }
    }

}

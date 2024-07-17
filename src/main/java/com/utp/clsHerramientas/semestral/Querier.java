package com.utp.clsHerramientas.semestral;

import java.util.ArrayList;

import com.utp.clsHerramientas.semestral.ui.Factura;

public final class Querier {
    public static Factura[] selectFacturas(Cliente cliente, Factura[] facturas) {
        ArrayList<Factura> facturasCliente = new ArrayList<Factura>();
        for (Factura fact : facturas) {
            if (fact.codigo().equals(cliente.codigo()))
                facturasCliente.add(fact);
        }
        return facturasCliente.toArray(new Factura[0]);
    }
}

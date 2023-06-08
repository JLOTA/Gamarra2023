
package com.Gamarra.app.Utils;

import org.springframework.stereotype.Component;

@Component
public class PedidoInforme {
    private int cantidadPedidos;
    private double gananciasTotales;

    public PedidoInforme() {
        this.cantidadPedidos = 0;
        this.gananciasTotales = 0.0;
    }

    public int getCantidadPedidos() {
        return cantidadPedidos;
    }

    public double getGananciasTotales() {
        return gananciasTotales;
    }

    public double getPromedioGanancias() {
        if (cantidadPedidos == 0) {
            return 0.0;
        }
        return gananciasTotales / cantidadPedidos;
    }

    public void incrementarCantidadPedidos() {
        cantidadPedidos++;
    }

    public void sumarGanancias(double ganancias) {
        gananciasTotales += ganancias;
    }
}

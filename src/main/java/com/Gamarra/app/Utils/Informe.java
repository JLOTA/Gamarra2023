package com.Gamarra.app.Utils;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class Informe {

    private int cantidad;
    private double gananciasTotales;
    private Map<String, Integer> serviciosSolicitados;

    public Informe() {
        this.cantidad = 0;
        this.gananciasTotales = 0.0;
        this.serviciosSolicitados = new HashMap<>();
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getGananciasTotales() {
        return gananciasTotales;
    }

    public double getPromedioGanancias() {
        if (cantidad == 0) {
            return 0.0;
        }
        return gananciasTotales / cantidad;
    }

    public void incrementarCantidad() {
        cantidad++;
    }

    public void sumarGanancias(double ganancias) {
        gananciasTotales += ganancias;
    }

    public void incrementarSolicitudesServicio(String servicio) {
        serviciosSolicitados.put(servicio, serviciosSolicitados.getOrDefault(servicio, 0) + 1);
    }

    public Map<String, Integer> getServiciosSolicitados() {
        return serviciosSolicitados;
    }
}

package com.Gamarra.app.Utils;

import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ReportesUtils {

    private Object entidad;
    private String entidadJson;
    private int cantidad;
    private double gananciaTotal;
    private double gananciaPromedio;

    @Override
    public String toString() {
        return "{"
                + "\"entidadJson\": " + entidadJson + ","
                + "\"cantidad\": " + cantidad + ","
                + "\"gananciaTotal\": " + gananciaTotal + ","
                + "\"gananciaPromedio\": " + gananciaPromedio
                + "}";
    }
}

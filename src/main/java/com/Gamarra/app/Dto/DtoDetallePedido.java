
package com.Gamarra.app.Dto;

import com.Gamarra.app.Negocio.Servicio;
import lombok.*;

@NoArgsConstructor
@Getter
@Setter
public class DtoDetallePedido {
    private Servicio servicio;
    private double cantidad;
    private String observacion;
    private double totalUnitario;

    public DtoDetallePedido(Servicio servicio, double cantidad, String observacion) {
        this.servicio = servicio;
        this.cantidad = cantidad;
        this.observacion = observacion;
    }
    
    public double getTotalUnitario(){
        return this.getServicio().getPrecio()*this.getCantidad();
    }
}

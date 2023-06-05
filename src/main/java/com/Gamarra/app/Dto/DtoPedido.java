
package com.Gamarra.app.Dto;

import com.Gamarra.app.Negocio.*;
import java.util.*;
import lombok.*;

@Getter
@Setter
public class DtoPedido {
    private String correlativo;
    private Date fecha;
    private Estado estado;
    private Usuario usuario;
    private Cliente cliente;
    private Empleado empleado;
    private List<DtoDetallePedido> detalles;

    public DtoPedido() {
        detalles = new ArrayList<>();
    }
    
    public void agregar(Servicio servicio, double cantidad, String observacion){
        DtoDetallePedido dtoDetalle= new DtoDetallePedido(servicio, cantidad, observacion);
        detalles.add(dtoDetalle);
    }
    
    public void quitar(int id) {
        Iterator<DtoDetallePedido> iterator = detalles.iterator();
        while (iterator.hasNext()) {
            DtoDetallePedido dtoDetalle = iterator.next();
            if (dtoDetalle.getServicio().getIdServicio() == id) {
                iterator.remove();
            }
        }
    }
    
    public double getSubtotal() {
        double tot = 0;
        for (int i = 0; i < detalles.size(); i++) {
            DtoDetallePedido dtoDetalle = (DtoDetallePedido) detalles.get(i);
            tot += dtoDetalle.getTotalUnitario();
        }
        return tot;
    }
}

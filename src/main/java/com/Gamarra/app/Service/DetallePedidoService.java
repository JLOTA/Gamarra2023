package com.Gamarra.app.Service;

import com.Gamarra.app.Dto.DtoDetallePedido;
import com.Gamarra.app.Dto.DtoPedido;
import com.Gamarra.app.Negocio.*;
import com.Gamarra.app.Persistencia.DetallePedidoRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DetallePedidoService {

    private final DetallePedidoRepository detallePedidoRepository;

    @Autowired
    public DetallePedidoService(DetallePedidoRepository detallePedidoRepository) {
        this.detallePedidoRepository = detallePedidoRepository;
    }

    public List<DetallePedido> listarDetalles() {
        return detallePedidoRepository.findAll();
    }

    public String grabarDetalle(DtoDetallePedido dtoDp, Pedido pedido) {
        DetallePedido detalle = new DetallePedido();
        detalle.setServicio(dtoDp.getServicio());
        detalle.setCantidad(dtoDp.getCantidad());
        detalle.setObservacion(dtoDp.getObservacion());
        detalle.setTotalUnitario(dtoDp.getTotalUnitario());
        detalle.setPedido(pedido);
        detallePedidoRepository.save(detalle);
        return "Detalle guardado: "+ detalle.getIdDetalle();
    }

    public String eliminarDetalle(DetallePedido detalle) {
        detallePedidoRepository.delete(detalle);
        return "Detalle eliminado";
    }

    public List<DetallePedido> listarDetallesPorPedido(Pedido pedido) {
        return detallePedidoRepository.findByPedido(pedido);
    }

}

package com.Gamarra.app.Service;

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

    public DetallePedido grabarDetalle(DetallePedido detalle) {
        return detallePedidoRepository.save(detalle);
    }

    public String eliminarDetalle(DetallePedido detalle) {
        detallePedidoRepository.delete(detalle);
        return "Detalle eliminado";
    }

    public List<DetallePedido> listarDetallesPorPedido(Pedido pedido) {
        return detallePedidoRepository.findByPedido(pedido);
    }

}

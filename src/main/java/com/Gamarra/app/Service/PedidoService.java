package com.Gamarra.app.Service;

import com.Gamarra.app.Negocio.Pedido;
import com.Gamarra.app.Persistencia.PedidoRepository;
import java.text.SimpleDateFormat;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;

    @Autowired
    public PedidoService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    public List<Pedido> listarPedidos() {
        return pedidoRepository.findAll();
    }

    public Pedido grabarPedido(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    public String eliminarPedido(Pedido pedido) {
        pedidoRepository.delete(pedido);
        return "Pedido eliminado";
    }

    public Pedido buscarPorId(int id) {
        return pedidoRepository.findById(id).orElse(null);
    }
    
    public String generarCorrelativo(){
        int numero=0;
        String correlativo="";
        
        List<Pedido> pedidos =this.listarPedidos();
        List<Integer> numeros= new ArrayList<Integer>();
        
        pedidos.stream().forEach(p -> numeros.add(p.getIdPedido()));
        
        if(pedidos.isEmpty()){
            numero=1;
        } else {
            numero=numeros.stream().max(Integer::compare).get();
            numero++;
        }
        
        correlativo = "P-" + String.format("%010d", numero);
        
        return correlativo;
    }
    
    public Date obtenerFecha() {
        SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String fechaString = formato.format(new Date());
        
        try {
            return formato.parse(fechaString);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

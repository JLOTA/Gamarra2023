package com.Gamarra.app.Service;

import com.Gamarra.app.Negocio.Venta;
import com.Gamarra.app.Persistencia.VentaRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VentaService {

    private final VentaRepository ventaRepository;

    @Autowired
    public VentaService(VentaRepository ventaRepository) {
        this.ventaRepository = ventaRepository;
    }
    
    public List<Venta> listarVentas(){
        return ventaRepository.findAll();
    }
}

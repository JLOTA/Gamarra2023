
package com.Gamarra.app.Service;

import com.Gamarra.app.Negocio.TipoVenta;
import com.Gamarra.app.Persistencia.TipoVentaRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TipoVentaService {
private final TipoVentaRepository tipoVentaRepository;

    @Autowired
    public TipoVentaService(TipoVentaRepository tipoVentaRepository) {
        this.tipoVentaRepository = tipoVentaRepository;
    }
    
    public List<TipoVenta> listarTipos(){ 
        return tipoVentaRepository.findAll();
    }
    
    public String grabarTipo(TipoVenta tipo){
        tipoVentaRepository.save(tipo);
        return "Tipo de venta guardado";
    }
}

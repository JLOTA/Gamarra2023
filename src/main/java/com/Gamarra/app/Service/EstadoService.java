
package com.Gamarra.app.Service;

import com.Gamarra.app.Negocio.Estado;
import com.Gamarra.app.Persistencia.EstadoRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EstadoService {
private final EstadoRepository estadoRepository;

    @Autowired
    public EstadoService(EstadoRepository estadoRepository) {
        this.estadoRepository = estadoRepository;
    }
    
    public List<Estado> listarEstados(){
        return estadoRepository.findAll();
    }
    
    public String grabarEstado(Estado estado){
        estadoRepository.save(estado);
        return "Estado grabado";
    }
    
    public Estado buscarPorId(int id){
        return estadoRepository.findById(id).orElse(null);
    }
}

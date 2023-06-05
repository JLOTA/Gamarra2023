package com.Gamarra.app.Service;

import com.Gamarra.app.Negocio.ClaseCliente;
import com.Gamarra.app.Persistencia.ClaseClienteRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClaseClienteService {

    private final ClaseClienteRepository claseClienteRepository;

    @Autowired
    public ClaseClienteService(ClaseClienteRepository claseClienteRepository) {
        this.claseClienteRepository = claseClienteRepository;
    }

    public String grabarClase(ClaseCliente claseCliente) {
        ClaseCliente claseClienteGuardada = claseClienteRepository.save(claseCliente);
        return "Clase guardada: " + claseClienteGuardada.getNombre();
    }

    public String eliminarClase(int idClaseCliente) {
        claseClienteRepository.deleteById(idClaseCliente);
        return "Clase eliminada: " + idClaseCliente;
    }

    public ClaseCliente buscarClasePorId(int idClaseCliente) {
        return claseClienteRepository.findById(idClaseCliente).orElse(null);
    }
    
    public List<ClaseCliente> listarClases() {
        return claseClienteRepository.findAll();
    }

}

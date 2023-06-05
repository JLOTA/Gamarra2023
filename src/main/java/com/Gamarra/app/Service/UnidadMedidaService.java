package com.Gamarra.app.Service;

import com.Gamarra.app.Negocio.UnidadMedida;
import com.Gamarra.app.Persistencia.UnidadMedidaRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UnidadMedidaService {

    private final UnidadMedidaRepository unidadMedidaRepository;

    @Autowired
    public UnidadMedidaService(UnidadMedidaRepository unidadMedidaRepository) {
        this.unidadMedidaRepository = unidadMedidaRepository;
    }

    public String grabarUnidadMedida(UnidadMedida unidad) {
        unidadMedidaRepository.save(unidad);
        return "Unidad de medida guardada: " + unidad.getNombre();
    }

    public UnidadMedida buscarUnidadMedidaPorId(int id) {
        return unidadMedidaRepository.findById(id).orElse(null);
    }

    public List<UnidadMedida> listarUnidadesMedida() {
        return unidadMedidaRepository.findAll();
    }

    public String eliminarUnidadMedida(UnidadMedida unidadMedida) {
        unidadMedidaRepository.delete(unidadMedida);
        return "Unidad de medida eliminada";
    }
}

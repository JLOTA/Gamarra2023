package com.Gamarra.app.Service;

import com.Gamarra.app.Negocio.UnidadMedida;
import com.Gamarra.app.Persistencia.UnidadMedidaRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UnidadMedidaService {

    private final UnidadMedidaRepository unidadMedidaRepository;

    @Autowired
    public UnidadMedidaService(UnidadMedidaRepository unidadMedidaRepository) {
        this.unidadMedidaRepository = unidadMedidaRepository;
    }

    public UnidadMedida buscarPorNombreOAbreviatura(String nombre, String abreviatura) {
        Optional<UnidadMedida> unidadOptional = unidadMedidaRepository.findByNombreOrAbreviatura(nombre, abreviatura);
        return unidadOptional.orElse(null);
    }

    public boolean grabarUnidadMedida(UnidadMedida unidad) {
        UnidadMedida unidadExistente = this.buscarPorNombreOAbreviatura(unidad.getNombre(), unidad.getAbreviatura());
        boolean respuesta;
        if (unidadExistente != null && (unidadExistente.getIdUnidad() != unidad.getIdUnidad())) {
            respuesta = false;
        } else {
            unidadMedidaRepository.save(unidad);
            respuesta = true;
        }
        return respuesta;
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

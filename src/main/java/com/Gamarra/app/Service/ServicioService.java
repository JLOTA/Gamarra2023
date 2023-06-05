package com.Gamarra.app.Service;

import com.Gamarra.app.Persistencia.ServicioRepository;
import com.Gamarra.app.Negocio.*;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServicioService {

    private final ServicioRepository servicioRepository;

    @Autowired
    public ServicioService(ServicioRepository servicioRepository) {
        this.servicioRepository = servicioRepository;
    }

    public boolean grabarServicio(Servicio servicio) {
        Servicio servicioExistente = this.buscarPorCategoriaYSubcategoria(servicio.getCategoria(), servicio.getSubcategoria());
        if (servicioExistente != null && (servicioExistente.getIdServicio()!= servicio.getIdServicio())) {
            return false;
        } else {
            servicioRepository.save(servicio);
            return true;
        }
    }

    public Servicio buscarServicioPorId(int id) {
        return servicioRepository.findById(id).orElse(null);
    }

    public List<Servicio> listarServicios() {
        return servicioRepository.findAll();
    }

    public String eliminarServicio(Servicio servicio) {
        servicioRepository.delete(servicio);
        return "Servicio eliminado";
    }
    
    public Servicio buscarPorCategoriaYSubcategoria(Categoria categoria, Subcategoria subcategoria){
        return servicioRepository.findByCategoriaAndSubcategoria(categoria, subcategoria);
    }
}

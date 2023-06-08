package com.Gamarra.app.Service;

import com.Gamarra.app.Persistencia.CategoriaRepository;
import com.Gamarra.app.Negocio.*;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    @Autowired
    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public Categoria buscarCategoriaPorId(int id) {
        return categoriaRepository.findById(id).orElse(null);
    }

    public List<Categoria> listarCategorias() {
        return categoriaRepository.findAll();
    }

    public Categoria buscarPorNombreOAbreviatura(String nombre, String abreviatura) {
        Optional<Categoria> categoriaOptional = categoriaRepository.findByNombreOrAbreviatura(nombre, abreviatura);
        return categoriaOptional.orElse(null);
    }

    public boolean grabarCategoria(Categoria categoria) {
        Categoria categoriaExistente = this.buscarPorNombreOAbreviatura(categoria.getNombre(), categoria.getAbreviatura());
        boolean respuesta;
        if (categoriaExistente != null && (categoriaExistente.getIdCategoria() != categoria.getIdCategoria())) {
            respuesta = false;
        } else {
            categoriaRepository.save(categoria);
            respuesta = true;
        }
        return respuesta;
    }

    public String eliminarCategoria(Categoria categoria) {
        categoriaRepository.delete(categoria);
        return "Empleado eliminado";
    }
}

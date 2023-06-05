
package com.Gamarra.app.Service;

import com.Gamarra.app.Persistencia.CategoriaRepository;
import com.Gamarra.app.Negocio.*;
import java.util.List;
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

    public String grabarCategoria(Categoria categoria) {
        categoriaRepository.save(categoria);
        return "Categoría actualizada: "+ categoria.getNombre();
    }

    public String eliminarCategoria(Categoria categoria) {
        categoriaRepository.delete(categoria);
        return "Empleado eliminado";
    }
}

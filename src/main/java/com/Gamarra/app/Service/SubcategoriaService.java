package com.Gamarra.app.Service;

import com.Gamarra.app.Negocio.Subcategoria;
import com.Gamarra.app.Persistencia.SubcategoriaRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubcategoriaService {

    private final SubcategoriaRepository subcategoriaRepository;

    @Autowired
    public SubcategoriaService(SubcategoriaRepository subcategoriaRepository) {
        this.subcategoriaRepository = subcategoriaRepository;
    }

    public String grabarSubcategoria(Subcategoria subcategoria) {
        subcategoriaRepository.save(subcategoria);
        return "Subcategoria guardada: "+subcategoria.getNombre();
    }

    public Subcategoria buscarSubcategoriaPorId(int id) {
        return subcategoriaRepository.findById(id).orElse(null);
    }

    public List<Subcategoria> listarSubcategorias() {
        return subcategoriaRepository.findAll();
    }

    public String eliminarSubcategoria(Subcategoria subcategoria) {
        subcategoriaRepository.delete(subcategoria);
        return "Subcategoria eliminada";
    }
}

package com.Gamarra.app.Service;

import com.Gamarra.app.Negocio.Subcategoria;
import com.Gamarra.app.Persistencia.SubcategoriaRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubcategoriaService {

    private final SubcategoriaRepository subcategoriaRepository;

    @Autowired
    public SubcategoriaService(SubcategoriaRepository subcategoriaRepository) {
        this.subcategoriaRepository = subcategoriaRepository;
    }

    public Subcategoria buscarPorNombreOAbreviatura(String nombre, String abreviatura) {
        Optional<Subcategoria> subcategoriaOptional = subcategoriaRepository.findByNombreOrAbreviatura(nombre, abreviatura);
        return subcategoriaOptional.orElse(null);
    }
    
    public boolean grabarSubcategoria(Subcategoria subcategoria) {
        Subcategoria subcategoriaExistente=this.buscarPorNombreOAbreviatura(subcategoria.getNombre(), subcategoria.getAbreviatura());
        if (subcategoriaExistente != null && (subcategoriaExistente.getIdSubcategoria()!= subcategoria.getIdSubcategoria())) {
            return false;
        } else {
            subcategoriaRepository.save(subcategoria);
            return true;
        }
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

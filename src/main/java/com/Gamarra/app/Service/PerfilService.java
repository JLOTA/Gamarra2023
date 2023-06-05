package com.Gamarra.app.Service;

import com.Gamarra.app.Negocio.Perfil;
import com.Gamarra.app.Persistencia.PerfilRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PerfilService {

    private final PerfilRepository perfilRepository;

    @Autowired
    public PerfilService(PerfilRepository perfilRepository) {
        this.perfilRepository = perfilRepository;
    }

    public String guardarPerfil(Perfil perfil) {
        Perfil perfilGuardado = perfilRepository.save(perfil);
        return "Perfil guardado: " + perfilGuardado.getNombre();
    }

    public Perfil buscarPerfilPorId(int id) {
        return perfilRepository.findById(id).orElse(null);
    }

    public List<Perfil> listarPerfiles() {
        return perfilRepository.findAll();
    }

    public String actualizarPerfil(Perfil perfil) {
        Perfil perfilActualizado = perfilRepository.save(perfil);
        return "Perfil actualizado: " + perfilActualizado.getIdPerfil();
    }

    public String eliminarPerfil(Perfil perfil) {
        perfilRepository.delete(perfil);
        return "Perfil eliminado";
    }
}

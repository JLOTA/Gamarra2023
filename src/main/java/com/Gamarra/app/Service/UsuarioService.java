package com.Gamarra.app.Service;

import com.Gamarra.app.Persistencia.UsuarioRepository;
import com.Gamarra.app.Negocio.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public String guardarUsuario(Usuario usuario) {
        if (usuario.getEmpleado() == null || usuario.getPerfil() == null) {
            return "No se pudo guardar el usuario. Empleado o perfil no encontrados.";
        } else {
            String claveEncriptada = passwordEncoder.encode(usuario.getClave());
            usuario.setClave(claveEncriptada);
            usuarioRepository.save(usuario);
            return "Usuario guardado: " + usuario;
        }
    }

    public Usuario buscarPorUsuario(String usuario) {
        return usuarioRepository.findByUsuario(usuario);
    }

    public Usuario buscarPorId(int id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    public Usuario validarUsuario(String usuario, String clave) {
        Usuario usuarioEncontrado = usuarioRepository.findByUsuario(usuario);

        if (usuarioEncontrado != null) {
            if (passwordEncoder.matches(clave, usuarioEncontrado.getClave())) {
                return usuarioEncontrado;
            }
        }
        return null;
    }
}

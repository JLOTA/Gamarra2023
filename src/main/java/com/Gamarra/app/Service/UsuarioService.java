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
    
    public boolean grabarUsuario(Usuario usuario) {
        Usuario usuarioExistente = this.buscarPorUsuario(usuario.getUsuario());
        if (usuarioExistente != null && (usuarioExistente.getIdUsuario()!= usuario.getIdUsuario())) {
            return false;
        } else {
            String claveEncriptada = passwordEncoder.encode(usuario.getClave());
            usuario.setClave(claveEncriptada);
            usuarioRepository.save(usuario);
            return true;
        }
    }

    public Usuario buscarPorUsuario(String usuario) {
        return usuarioRepository.findByUsuario(usuario);
    }
    
    public Usuario buscarPorEmpleado(Empleado empleado) {
        return usuarioRepository.findByEmpleado(empleado);
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

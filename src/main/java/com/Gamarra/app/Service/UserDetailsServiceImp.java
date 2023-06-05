package com.Gamarra.app.Service;

import com.Gamarra.app.Negocio.Usuario;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImp implements UserDetailsService {

    private final UsuarioService usuarioService;

    public UserDetailsServiceImp (UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioService.buscarPorUsuario(username);
        if (usuario == null) {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }

        return org.springframework.security.core.userdetails.User
                .withUsername(usuario.getUsuario())
                .password(usuario.getClave())
                .roles(usuario.getPerfil().getNombre())
                .build();
    }
}

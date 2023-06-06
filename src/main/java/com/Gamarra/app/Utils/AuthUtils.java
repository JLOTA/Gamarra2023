package com.Gamarra.app.Utils;

import com.Gamarra.app.Negocio.Usuario;
import com.Gamarra.app.Service.UsuarioService;
import java.security.Principal;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

@Component
public class AuthUtils {

    private UsuarioService usuarioService;

    public AuthUtils(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    public boolean usuarioLogeado(Model model, Principal principal) {
        if (principal != null) {
            String username = principal.getName();
            Usuario usuarioLog = usuarioService.buscarPorUsuario(username);
            model.addAttribute("usuarioLog", usuarioLog);
            return true;
        } else {
            return false;
        }
    }
}

package com.Gamarra.app.Control;

import com.Gamarra.app.Negocio.*;
import com.Gamarra.app.Service.*;
import org.springframework.ui.Model;
import java.util.*;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.*;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class Control {

    private final EmpleadoService empleadoService;
    private final PerfilService perfilService;
    private final UsuarioService usuarioService;
    private final VentaService ventaService;
    private final UserDetailsService userDetailsService;

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

    @GetMapping("/")
    public String showHomePage() {
        Usuario usuario = new Usuario();
        usuario.setEmpleado(empleadoService.buscarEmpleadoPorId(1));
        usuario.setPerfil(perfilService.buscarPerfilPorId(1));
        usuario.setUsuario("JOTA72");
        usuario.setClave("JOTA72JOTA72");
        usuarioService.guardarUsuario(usuario);
        return "usuariosform";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(Model model, @RequestParam("username") String username, @RequestParam("password") String password) {
        Usuario usuario = usuarioService.validarUsuario(username, password);
        if (usuario != null) {
            userDetailsService.loadUserByUsername(username);
            return "redirect:/ventas";
        } else {
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return "redirect:/login?logout";
    }

    /*-----------------------*/
    @GetMapping("/ventas")
    public String mostrarVentas(Model model, Principal principal) {
        if (usuarioLogeado(model, principal)) {
            List<Venta> ventas = ventaService.listarVentas();
            model.addAttribute("ventas", ventas);
            return "ventas";
        } else {
            model.addAttribute("errorMessage", "Usuario no encontrado");
            return "redirect:/login";
        }
    }

}

package com.Gamarra.app.Control;

import com.Gamarra.app.Negocio.*;
import com.Gamarra.app.Service.*;
import com.Gamarra.app.Utils.AuthUtils;
import org.springframework.ui.Model;
import java.util.*;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.*;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class Control {

    private final UsuarioService usuarioService;
    private final VentaService ventaService;
    private final AuthUtils authUtils;
    private final UserDetailsService userDetailsService;

    @GetMapping("/home")
    public String showHomePage(Model model, Principal principal) {
        if (authUtils.usuarioLogeado(model, principal)) {

            model.addAttribute("ventasDiarias", ventaService.obtenerVentasDiarias());
            model.addAttribute("ventasSemanales", ventaService.obtenerVentasSemanales());
            model.addAttribute("ventasMensuales", ventaService.obtenerVentasMensuales());
            model.addAttribute("serviciosDelMes", ventaService.obtenerServiciosMasVendidosDelMes());

            return "home";
        } else {
            model.addAttribute("errorMessage", "Usuario no encontrado");
            return "redirect:/login";
        }
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(Model model, @RequestParam("username") String username, @RequestParam("password") String password) {
        Usuario usuario = usuarioService.validarUsuario(username, password);
        if (usuario != null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
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

}

package com.Gamarra.app.Control;

import com.Gamarra.app.Negocio.*;
import com.Gamarra.app.Service.*;
import com.Gamarra.app.Utils.*;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequiredArgsConstructor
@Controller
@RequestMapping("/empleados")
public class EmpleadoControl {

    private final AuthUtils authUtils;
    private final EmpleadoService empleadoService;
    private final UsuarioService usuarioService;
    private final PerfilService perfilService;

    @GetMapping("/")
    public String mostrarEmpleados(Model model, Principal principal) {
        if (authUtils.usuarioLogeado(model, principal)) {
            return "empleados";
        } else {
            model.addAttribute("errorMessage", "Usuario no encontrado");
            return "redirect:/login";
        }
    }

    @GetMapping("/listar")
    public String listarEmpleados(Model model) {
        List<Empleado> empleados = empleadoService.listarEmpleados();
        model.addAttribute("empleados", empleados);
        return "fragmentos/tablas :: tablaEmpleado";
    }

    @PostMapping("/grabar")
    public String grabarEmpleado(@ModelAttribute("empleado") Empleado empleado, RedirectAttributes redirectAttributes) {
        if (empleadoService.grabarEmpleado(empleado)) {
            redirectAttributes.addFlashAttribute("successMessage", "Empleado registrado exitosamente!!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "El DNI ingresado ya está registrado");
        }
        return "redirect:/empleados/";
    }

    @GetMapping("/borrar")
    public String borrarEmpleado(@RequestParam(value = "id") Empleado empleado, Model model, Principal principal) {
        if (authUtils.usuarioLogeado(model, principal)) {
            empleadoService.eliminarEmpleado(empleado);
            return "redirect:/empleados/";
        } else {
            model.addAttribute("errorMessage", "Usuario no encontrado");
            return "redirect:/login";
        }
    }

    @GetMapping("/form")
    public String formEmpleados(@RequestParam(value = "id", required = false) Integer idEmpleado, Model model) {
        Empleado empleado = null;
        if (idEmpleado != null) {
            empleado = empleadoService.buscarEmpleadoPorId(idEmpleado);
        }
        if (empleado != null) {
            model.addAttribute("titulo", "Editar Empleado");
        } else {
            empleado = new Empleado();
            model.addAttribute("titulo", "Nuevo Empleado");
        }
        model.addAttribute("empleado", empleado);
        return "fragmentos/modals :: modalEmpleado";
    }

    @GetMapping("/asignar-usuario")
    public String asignarUsuario(@RequestParam(value = "id", required = false) Integer idEmpleado, Model model) {
        Empleado empleado = empleadoService.buscarEmpleadoPorId(idEmpleado);
        Usuario usuario = usuarioService.buscarPorEmpleado(empleado);
        String msg = "";
        if (usuario != null) {
            msg = "El empleado ya tiene un usuario asignado" + usuario.getUsuario();
        } else {
            usuario = new Usuario();
            usuario.setEmpleado(empleado);
            msg = "Nueva asignacion";
        }
        model.addAttribute("msg", msg);
        model.addAttribute("perfiles", perfilService.listarPerfiles());
        model.addAttribute("empleado", empleado);
        model.addAttribute("user", usuario);
        return "fragmentos/modals :: modalAsignarUsuario";
    }
    
    @PostMapping("/grabar-usuario")
    public String grabarUsuario(@ModelAttribute("user") Usuario usuario, RedirectAttributes redirectAttributes) {
        if (usuarioService.grabarUsuario(usuario)) {
            redirectAttributes.addFlashAttribute("successMessage", "Usuario registrado exitosamente!!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "El usuario ingresado ya está registrado");
        }
        return "redirect:/empleados/";
    }

    @GetMapping("/buscar")
    public String buscarEmpleados(@RequestParam("dni") String dni, Model model) {
        List<Empleado> empleados = empleadoService.buscarEmpleadosPorDni(dni);
        model.addAttribute("empleados", empleados);
        return "fragmentos/tablas :: tablaEmpleado";
    }

}

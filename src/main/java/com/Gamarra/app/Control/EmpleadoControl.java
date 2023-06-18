package com.Gamarra.app.Control;

import com.Gamarra.app.Negocio.Empleado;
import com.Gamarra.app.Service.EmpleadoService;
import com.Gamarra.app.Utils.AuthUtils;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequiredArgsConstructor
@Controller
@RequestMapping("/empleados")
public class EmpleadoControl {

    private final AuthUtils authUtils;
    private final EmpleadoService empleadoService;

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
    
    @GetMapping("/buscar")
    public String buscarEmpleados(@RequestParam("dni") String dni, Model model) {
        List<Empleado> empleados = empleadoService.buscarEmpleadosPorDni(dni);
        model.addAttribute("empleados", empleados);
        return "fragmentos/tablas :: tablaEmpleado";
    }

}

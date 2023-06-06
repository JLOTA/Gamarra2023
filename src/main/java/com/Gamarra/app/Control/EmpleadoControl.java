
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

@RequiredArgsConstructor
@Controller
@RequestMapping("/empleados")
public class EmpleadoControl {
    private final AuthUtils authUtils;
    private final EmpleadoService empleadoService;
    
    @GetMapping("/")
    public String mostrarEmpleados(Model model, Principal principal) {
        if (authUtils.usuarioLogeado(model, principal)) {
            List<Empleado> empleados = empleadoService.listarEmpleados();
            model.addAttribute("empleados", empleados);
            return "empleados";
        } else {
            model.addAttribute("errorMessage", "Usuario no encontrado");
            return "redirect:/login";
        }
    }

    @GetMapping("/form")
    public String formEmpleados(@RequestParam(value = "id", required = false) Empleado empleado, Model model, Principal principal) {
        if (authUtils.usuarioLogeado(model, principal)) {
            if (empleado != null) {
                model.addAttribute("editar", true);
            } else {
                empleado = new Empleado();
            }
            model.addAttribute("empleado", empleado);
            return "empleadosform";
        } else {
            model.addAttribute("errorMessage", "Usuario no encontrado");
            return "redirect:/login";
        }
    }

    @PostMapping("/grabar")
    public String grabarEmpleado(@ModelAttribute("empleado") Empleado empleado, Model model, Principal principal) {
        if (authUtils.usuarioLogeado(model, principal)) {
            if (empleadoService.grabarEmpleado(empleado)) {
                return "redirect:/empleados/";
            } else {
                model.addAttribute("errorMessage", "Ya existe un empleado con el mismo DNI");
                return "empleadosform";
            }
        } else {
            model.addAttribute("errorMessage", "Usuario no encontrado");
            return "redirect:/login";
        }
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
}

package com.Gamarra.app.Control;

import com.Gamarra.app.Negocio.*;
import com.Gamarra.app.Service.*;
import com.Gamarra.app.Utils.AuthUtils;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequiredArgsConstructor
@Controller
@RequestMapping("/servicios")
public class ServicioControl {

    private final AuthUtils authUtils;
    private final CategoriaService categoriaService;
    private final ServicioService servicioService;
    private final SubcategoriaService subcategoriaService;
    private final UnidadMedidaService unidadService;
    private final UsuarioService usuarioService;

    @GetMapping("/")
    public String mostrarServicios(Model model, Principal principal) {
        if (authUtils.usuarioLogeado(model, principal)) {
            return "servicios";
        } else {
            model.addAttribute("errorMessage", "Usuario no encontrado");
            return "redirect:/login";
        }
    }

    @GetMapping("/listar")
    public String listarServicios(Model model) {
        List<Servicio> servicios = servicioService.listarServicios();
        model.addAttribute("servicios", servicios);
        return "fragmentos/tablas :: tablaServicio";
    }

    @GetMapping("/form")
    public String formServicio(@RequestParam(value = "id", required = false) Integer idServicio, Model model) {
        Servicio servicio = null;
        if (idServicio != null) {
            servicio = servicioService.buscarServicioPorId(idServicio);
        }
        if (servicio != null) {
            model.addAttribute("titulo", "Editar Servicio");
        } else {
            servicio = new Servicio();
            model.addAttribute("titulo", "Nuevo Servicio");
        }
        model.addAttribute("categorias", categoriaService.listarCategorias());
        model.addAttribute("subcategorias", subcategoriaService.listarSubcategorias());
        model.addAttribute("unidades", unidadService.listarUnidadesMedida());
        model.addAttribute("servicio", servicio);
        return "fragmentos/modals :: modalServicio";
    }

    @PostMapping("/grabar")
    public String grabarServicio(@ModelAttribute("servicio") Servicio servicio, RedirectAttributes redirectAttributes, Principal principal) {
        servicio.setUsuario(usuarioService.buscarPorUsuario(principal.getName()));
        if (servicioService.grabarServicio(servicio)) {
            redirectAttributes.addFlashAttribute("successMessage", "Servicio registrado exitosamente!!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "El Servicio ingresado ya está registrado");
        }
        return "redirect:/servicios/";
    }
    
    @GetMapping("/buscar")
    public String buscarServicio(@RequestParam("abreviatura") String abreviatura, Model model) {
        List<Servicio> servicios = servicioService.buscarPorAbrevitura(abreviatura);
        model.addAttribute("servicios", servicios);
        return "fragmentos/tablas :: tablaServicio";
    }

    @PostMapping("/grabarCategoria")
    public String grabarCategoria(@ModelAttribute("categoria") Categoria categoria, Model model, Principal principal) {
        if (authUtils.usuarioLogeado(model, principal)) {
            if (categoriaService.grabarCategoria(categoria)) {
                return "redirect:/servicios/";
            } else {
                List<Servicio> servicios = servicioService.listarServicios();
                model.addAttribute("servicios", servicios);
                model.addAttribute("errorMessage", "Categoria ya se encuentra registrada: " + categoria.getNombre());
                return "servicios";
            }
        } else {
            model.addAttribute("errorMessage", "Usuario no encontrado");
            return "redirect:/login";
        }
    }

    @PostMapping("/grabarSubcategoria")
    public String grabarSubcategoria(@ModelAttribute("subcategoria") Subcategoria subcategoria, Model model, Principal principal) {
        if (authUtils.usuarioLogeado(model, principal)) {
            if (subcategoriaService.grabarSubcategoria(subcategoria)) {
                return "redirect:/servicios/";
            } else {
                List<Servicio> servicios = servicioService.listarServicios();
                model.addAttribute("servicios", servicios);
                model.addAttribute("errorMessage", "Subcategoria ya se encuentra registrada: " + subcategoria.getNombre());
                return "servicios";
            }
        } else {
            model.addAttribute("errorMessage", "Usuario no encontrado");
            return "redirect:/login";
        }
    }

    @PostMapping("/grabarUnidad")
    public String grabarUnidadMedida(@ModelAttribute("unidad") UnidadMedida unidad, Model model, Principal principal) {
        if (authUtils.usuarioLogeado(model, principal)) {
            if (unidadService.grabarUnidadMedida(unidad)) {
                return "redirect:/servicios/";
            } else {
                List<Servicio> servicios = servicioService.listarServicios();
                model.addAttribute("servicios", servicios);
                model.addAttribute("errorMessage", "Unidad de Medida ya se encuentra registrada: " + unidad.getNombre());
                return "servicios";
            }
        } else {
            model.addAttribute("errorMessage", "Usuario no encontrado");
            return "redirect:/login";
        }
    }

    @GetMapping("/borrar")
    public String borrarServicio(@RequestParam(value = "id") Servicio servicio, Model model, Principal principal) {
        if (authUtils.usuarioLogeado(model, principal)) {
            servicioService.eliminarServicio(servicio);
            return "redirect:/servicios/";
        } else {
            model.addAttribute("errorMessage", "Usuario no encontrado");
            return "redirect:/login";
        }
    }
}

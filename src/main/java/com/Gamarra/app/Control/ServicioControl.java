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
            List<Servicio> servicios = servicioService.listarServicios();
            model.addAttribute("servicios", servicios);
            return "servicios";
        } else {
            model.addAttribute("errorMessage", "Usuario no encontrado");
            return "redirect:/login";
        }
    }

    @GetMapping("/form")
    public String formServicio(@RequestParam(value = "idServicio", required = false) Servicio servicio, Model model, Principal principal) {
        if (authUtils.usuarioLogeado(model, principal)) {
            if (servicio != null) {
                model.addAttribute("editar", true);
            } else {
                servicio = new Servicio();
            }
            model.addAttribute("categorias", categoriaService.listarCategorias());
            model.addAttribute("subcategorias", subcategoriaService.listarSubcategorias());
            model.addAttribute("unidades", unidadService.listarUnidadesMedida());
            model.addAttribute("servicio", servicio);
            return "serviciosform";
        } else {
            model.addAttribute("errorMessage", "Usuario no encontrado");
            return "redirect:/login";
        }
    }

    @PostMapping("/grabar")
    public String guardarServicio(@ModelAttribute("servicio") Servicio servicio, Model model, Principal principal) {
        if (authUtils.usuarioLogeado(model, principal)) {
            servicio.setUsuario(usuarioService.buscarPorUsuario(principal.getName()));
            if (servicioService.grabarServicio(servicio)) {
                return "redirect:/servicios/";
            } else {
                model.addAttribute("categorias", categoriaService.listarCategorias());
                model.addAttribute("subcategorias", subcategoriaService.listarSubcategorias());
                model.addAttribute("unidades", unidadService.listarUnidadesMedida());
                model.addAttribute("servicio", servicio);
                model.addAttribute("errorMessage", "Ya existe un este servicio: " + servicio.getCategoria().getAbreviatura() + "-" + servicio.getSubcategoria().getAbreviatura());
                return "serviciosform";
            }
        } else {
            model.addAttribute("errorMessage", "Usuario no encontrado");
            return "redirect:/login";
        }
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

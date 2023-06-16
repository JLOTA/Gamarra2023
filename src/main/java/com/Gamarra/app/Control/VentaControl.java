package com.Gamarra.app.Control;

import com.Gamarra.app.Negocio.*;
import com.Gamarra.app.Service.*;
import com.Gamarra.app.Utils.*;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
@RequestMapping("/ventas")
public class VentaControl {

    private final AuthUtils authUtils;
    private final VentaService ventaService;

    @GetMapping("/")
    public String mostrarVentas(Model model, Principal principal) {
        if (authUtils.usuarioLogeado(model, principal)) {
            List<Venta> ventas = ventaService.listarVentas();
            model.addAttribute("ventas", ventas);
            return "ventas";
        } else {
            model.addAttribute("errorMessage", "Usuario no encontrado");
            return "redirect:/login";
        }
    }

    @GetMapping("/generar")
    public String generarVenta(@RequestParam(value = "id") Pedido pedido, Model model, Principal principal) {
        if (authUtils.usuarioLogeado(model, principal)) {
            if (pedido != null) {
                ventaService.nuevaVenta(principal.getName(), pedido);
                model.addAttribute("venta", ventaService.verVenta());
                model.addAttribute("detalles", ventaService.verVenta().getPedido().getDetalles());
                return "ventasgenerar";
            } else {
                return "redirect:/pedidos/";
            }
        } else {
            model.addAttribute("errorMessage", "Usuario no encontrado");
            return "redirect:/login";
        }
    }

    @PostMapping("/grabar")
    public String grabarPedido(@RequestParam(value = "idTipo", required = false) Integer idTipo,
            @RequestParam(value = "descuento", required = false) Double descuento,
            Model model, Principal principal) {
        if (authUtils.usuarioLogeado(model, principal)) {
            if (idTipo != null && descuento != null) {
                ventaService.grabarVenta(idTipo, descuento);
            } else if (idTipo != null && descuento == null) {
                ventaService.grabarVenta(idTipo, 0);
            } else if (idTipo == null && descuento != null) {
                ventaService.grabarVenta(1, descuento);
            } else {
                ventaService.grabarVenta(1, 0);
            }
            return "redirect:/ventas/";
        } else {
            model.addAttribute("errorMessage", "Usuario no encontrado");
            return "redirect:/login";
        }
    }

    @GetMapping("/reporte")
    public String obtenerReportes(Model model, Principal principal, @PageableDefault(size = 10) Pageable pageable) {
        if (authUtils.usuarioLogeado(model, principal)) {

            model.addAttribute("ventasDiarias", ventaService.obtenerVentasDiarias());
            model.addAttribute("ventasSemanales", ventaService.obtenerVentasSemanales());
            model.addAttribute("ventasMensuales", ventaService.obtenerVentasMensuales());

            return "ventasreport";
        } else {
            model.addAttribute("errorMessage", "Usuario no encontrado");
            return "redirect:/login";
        }
    }
    
    @GetMapping("/ver")
    public String mostrarVenta(@RequestParam(value = "id") Venta venta, Model model, Principal principal) {
        if (authUtils.usuarioLogeado(model, principal)) {
            if (venta != null) {
                model.addAttribute("venta", venta);
                model.addAttribute("detalles", venta.getPedido().getDetalles());
                return "ventasvista";
            } else {
                return "redirect:/ventas/";
            }
        } else {
            model.addAttribute("errorMessage", "Usuario no encontrado");
            return "redirect:/login";
        }
    }
}

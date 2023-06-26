package com.Gamarra.app.Control;

import com.Gamarra.app.Negocio.*;
import com.Gamarra.app.Service.*;
import com.Gamarra.app.Utils.*;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RequiredArgsConstructor
@Controller
@RequestMapping("/ventas")
public class VentaControl {

    private final AuthUtils authUtils;
    private final PedidoService pedidoService;
    private final VentaService ventaService;

    @GetMapping("/")
    public String mostrarVentas(Model model, Principal principal, @PageableDefault(size = 10) Pageable pageable) {
        if (authUtils.usuarioLogeado(model, principal)) {
            return "ventas";
        } else {
            model.addAttribute("errorMessage", "Usuario no encontrado");
            return "redirect:/login";
        }
    }

    @GetMapping("/generar")
    public String generarVenta(@RequestParam(value = "id") Integer idPedido, Model model, Principal principal) {
        Pedido pedido = pedidoService.buscarPorId(idPedido);
        ventaService.nuevaVenta(principal.getName(), pedido);
        model.addAttribute("venta", ventaService.verVenta());
        model.addAttribute("detalles", ventaService.verVenta().getPedido().getDetalles());
        return "fragmentos/vistas :: modalVentaGenerar";
    }

    @PostMapping("/grabar")
    public String grabarVenta(@RequestParam(value = "idTipo", required = false) Integer idTipo,
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
            return "ventasreport";
        } else {
            model.addAttribute("errorMessage", "Usuario no encontrado");
            return "redirect:/login";
        }
    }

    @GetMapping("/ver")
    public String mostrarVenta(@RequestParam(value = "id") Integer idVenta, Model model, Principal principal) {
        Venta venta = ventaService.buscarPorId(idVenta);
        model.addAttribute("venta", venta);
        model.addAttribute("detalles", venta.getPedido().getDetalles());
        return "fragmentos/vistas :: modalVentaVista";
    }

    @GetMapping("/buscar")
    public String buscarVentas(@RequestParam("correlativo") String correlativo, Model model, @PageableDefault(size = 10) Pageable pageable) {
        Page<Venta> ventasPage = ventaService.buscarPorCorrelativo(pageable, correlativo);
        if (ventasPage != null && ventasPage.hasContent()) {
            model.addAttribute("ventas", ventasPage.getContent());
            model.addAttribute("currentPage", ventasPage.getNumber());
            model.addAttribute("totalPages", ventasPage.getTotalPages());

            String url = ServletUriComponentsBuilder.fromCurrentRequest()
                    .replaceQueryParam("page", "{page}")
                    .buildAndExpand("{page}")
                    .toUriString();
            model.addAttribute("url", url);
        }
        return "fragmentos/tablas :: tablaVenta";
    }

    @GetMapping("/listar")
    public String listarVentas(Model model, @PageableDefault(size = 10) Pageable pageable) {
        Page<Venta> ventasPage = ventaService.obtenerVentasPaginados(pageable);
        if (ventasPage != null && ventasPage.hasContent()) {
            model.addAttribute("ventas", ventasPage.getContent());
            model.addAttribute("currentPage", ventasPage.getNumber());
            model.addAttribute("totalPages", ventasPage.getTotalPages());

            String url = ServletUriComponentsBuilder.fromCurrentRequest()
                    .replaceQueryParam("page", "{page}")
                    .buildAndExpand("{page}")
                    .toUriString();
            model.addAttribute("url", url);
        }
        return "fragmentos/tablas :: tablaVenta";
    }

    @GetMapping("/reporte/MA")
    public String reportesPorMes(Model model, @RequestParam("monthYear") String fecha) {
        List<Venta> filtro = ventaService.obtenerVentasPorMes(fecha);
        model.addAttribute("ventas", filtro);
        model.addAttribute("titulo", ventaService.obtenerPeriodo(fecha));
        List<ReportesUtils> clientes=ventaService.reporteClienteDetalles(filtro);
        List<ReportesUtils> empleados=ventaService.reporteEmpleadoDetalles(filtro);
        List<ReportesUtils> servicios=ventaService.reporteServicioDetalles(filtro);
        ReportesUtils detalles=ventaService.reporteVentasDetalles(filtro);
        model.addAttribute("detalle", detalles);
        model.addAttribute("detalleJson", detalles.toString());
        model.addAttribute("clientes", clientes);
        model.addAttribute("clientesJson", ventaService.reportesToJson(clientes));
        model.addAttribute("empleados", empleados);
        model.addAttribute("empleadosJson", ventaService.reportesToJson(empleados));
        model.addAttribute("servicios", servicios);
        model.addAttribute("serviciosJson", ventaService.reportesToJson(servicios));
        return "fragmentos/reportes :: mesAnioTabla";
    }
}

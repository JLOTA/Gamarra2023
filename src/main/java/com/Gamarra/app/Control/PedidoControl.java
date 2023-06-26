package com.Gamarra.app.Control;

import com.Gamarra.app.Negocio.*;
import com.Gamarra.app.Service.*;
import com.Gamarra.app.Utils.*;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RequiredArgsConstructor
@Controller
@RequestMapping("/pedidos")
public class PedidoControl {

    private final AuthUtils authUtils;
    private final PedidoService pedidoService;
    private final DetallePedidoService detalleService;
    private final ServicioService servicioService;

    @GetMapping("/")
    public String mostrarPedidos(Model model, Principal principal) {
        if (authUtils.usuarioLogeado(model, principal)) {
            return "pedidos";
        } else {
            model.addAttribute("errorMessage", "Usuario no encontrado");
            return "redirect:/login";
        }
    }

    @GetMapping("/form")
    public String formPedido(Model model, Principal principal) {
        if (authUtils.usuarioLogeado(model, principal)) {
            pedidoService.nuevoPedido(principal.getName());
            model.addAttribute("servicios", servicioService.listarServicios());
            model.addAttribute("detalles", pedidoService.verCarrito());
            model.addAttribute("pedido", pedidoService.verPedido());
            return "pedidosform";
        } else {
            model.addAttribute("errorMessage", "Usuario no encontrado");
            return "redirect:/login";
        }
    }

    @GetMapping("/cartd")
    public String cartPedidoDetalle(@RequestParam(value = "idServicio") int idServicio,
            @RequestParam(value = "cantidad") double cantidad,
            @RequestParam(value = "observacion") String observacion,
            Model model) {
        model.addAttribute("msg", pedidoService.agregarServicio(idServicio, cantidad, observacion));
        model.addAttribute("servicios", servicioService.listarServicios());
        model.addAttribute("detalles", pedidoService.verCarrito());
        model.addAttribute("pedido", pedidoService.verPedido());

        return "fragmentos/carrito :: divPedidoDetalle";
    }

    @GetMapping("/carte")
    public String cartPedidoEmpleado(@RequestParam(value = "dni") String dni, Model model) {
        pedidoService.asignarEmpleado(dni);
        model.addAttribute("servicios", servicioService.listarServicios());
        model.addAttribute("detalles", pedidoService.verCarrito());
        model.addAttribute("pedido", pedidoService.verPedido());
        return "fragmentos/carrito :: divPedidoEmpleado";
    }

    @GetMapping("/cartc")
    public String cartPedidoCliente(@RequestParam(value = "documento") String documento, Model model) {
        pedidoService.asignarCliente(documento);
        model.addAttribute("servicios", servicioService.listarServicios());
        model.addAttribute("detalles", pedidoService.verCarrito());
        model.addAttribute("pedido", pedidoService.verPedido());
        return "fragmentos/carrito :: divPedidoCliente";
    }

    @GetMapping("/cartq")
    public String quitarServicioCart(@RequestParam(value = "observacion") String observacion, 
            @RequestParam(value = "id") Integer idServicio, Model model) {
        pedidoService.quitarServicio(idServicio, observacion);
        model.addAttribute("servicios", servicioService.listarServicios());
        model.addAttribute("detalles", pedidoService.verCarrito());
        model.addAttribute("pedido", pedidoService.verPedido());
        return "fragmentos/carrito :: divPedidoDetalle";
    }

    @GetMapping("/grabar")
    public String grabarPedido(Model model, Principal principal) {
        if (authUtils.usuarioLogeado(model, principal)) {
            pedidoService.grabarPedidoConDetalles();
            return "redirect:/pedidos/";
        } else {
            model.addAttribute("errorMessage", "Usuario no encontrado");
            return "redirect:/login";
        }
    }

    @GetMapping("/ver")
    public String mostrarPedido(@RequestParam(value = "id") Integer idPedido, Model model) {
        Pedido pedido = pedidoService.buscarPorId(idPedido);
        model.addAttribute("pedido", pedido);
        model.addAttribute("detalles", pedido.getDetalles());
        return "fragmentos/vistas :: modalPedidoVista";
    }

    @GetMapping("/resumen")
    public String mostrarResumen(Model model, Principal principal) {
        if (authUtils.usuarioLogeado(model, principal)) {
            model.addAttribute("pedido", pedidoService.verPedido());
            model.addAttribute("detalles", pedidoService.verCarrito());
            return "pedidosresumen";
        } else {
            model.addAttribute("errorMessage", "Usuario no encontrado");
            return "redirect:/login";
        }
    }

    @GetMapping("/reporte")
    public String obtenerReportes(Model model, Principal principal, @PageableDefault(size = 10) Pageable pageable) {
        if (authUtils.usuarioLogeado(model, principal)) {
            model.addAttribute("pedidosDiarios", pedidoService.obtenerPedidosDiarios());
            model.addAttribute("pedidosSemanales", pedidoService.obtenerPedidosSemanales());
            model.addAttribute("pedidosMensuales", pedidoService.obtenerPedidosMensuales());
            model.addAttribute("serviciosDelMes", pedidoService.obtenerServiciosMasPedidosDelMes());

            return "pedidosreport";
        } else {
            model.addAttribute("errorMessage", "Usuario no encontrado");
            return "redirect:/login";
        }
    }

    @GetMapping("/buscar")
    public String buscarPedidos(@RequestParam("correlativo") String correlativo, Model model, @PageableDefault(size = 10) Pageable pageable) {
        Page<Pedido> pedidosPage = pedidoService.buscarPorCorrelativo(pageable, correlativo);
        if (pedidosPage != null && pedidosPage.hasContent()) {
            model.addAttribute("pedidos", pedidosPage.getContent());
            model.addAttribute("currentPage", pedidosPage.getNumber());
            model.addAttribute("totalPages", pedidosPage.getTotalPages());

            String url = ServletUriComponentsBuilder.fromCurrentRequest()
                    .replaceQueryParam("page", "{page}")
                    .buildAndExpand("{page}")
                    .toUriString();
            model.addAttribute("url", url);
        }
        return "fragmentos/tablas :: tablaPedido";
    }

    @GetMapping("/listar")
    public String listarPedidos(Model model, @PageableDefault(size = 10) Pageable pageable) {
        Page<Pedido> pedidosPage = pedidoService.obtenerPedidosPaginados(pageable);
        if (pedidosPage != null && pedidosPage.hasContent()) {
            model.addAttribute("pedidos", pedidosPage.getContent());
            model.addAttribute("currentPage", pedidosPage.getNumber());
            model.addAttribute("totalPages", pedidosPage.getTotalPages());

            String url = ServletUriComponentsBuilder.fromCurrentRequest()
                    .replaceQueryParam("page", "{page}")
                    .buildAndExpand("{page}")
                    .toUriString();
            model.addAttribute("url", url);
        }
        return "fragmentos/tablas :: tablaPedido";
    }

}

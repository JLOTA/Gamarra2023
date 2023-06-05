package com.Gamarra.app.Control;

import com.Gamarra.app.Negocio.*;
import com.Gamarra.app.Service.*;
import org.springframework.ui.Model;
import java.util.*;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.*;
import java.security.Principal;
import java.util.stream.Collectors;
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
    private final CategoriaService categoriaService;
    private final ClaseClienteService claseService;
    private final ClienteService clienteService;

    private final DetallePedidoService detalleService;
    
    List<DetallePedido> detalles = new ArrayList<DetallePedido>();
    Pedido pedido = new Pedido();

    private final EstadoService estadoService;
    private final PedidoService pedidoService;
    private final PerfilService perfilService;
    private final ServicioService servicioService;
    private final SubcategoriaService subcategoriaService;
    private final TipoVentaService tipoService;
    private final UnidadMedidaService unidadService;
    private final UsuarioService usuarioService;
    private final VentaService ventaService;
    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder passwordEncoder;

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

    @GetMapping("/empleados")
    public String mostrarEmpleados(Model model, Principal principal) {
        if (usuarioLogeado(model, principal)) {
            List<Empleado> empleados = empleadoService.listarEmpleados();
            model.addAttribute("empleados", empleados);
            return "empleados";
        } else {
            model.addAttribute("errorMessage", "Usuario no encontrado");
            return "redirect:/login";
        }
    }

    @GetMapping("/empleados/form")
    public String formEmpleados(@RequestParam(value = "id", required = false) Empleado empleado, Model model, Principal principal) {
        if (usuarioLogeado(model, principal)) {
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

    @PostMapping("/empleados/grabar")
    public String grabarEmpleado(@ModelAttribute("empleado") Empleado empleado, Model model, Principal principal) {
        if (usuarioLogeado(model, principal)) {
            if (empleadoService.grabarEmpleado(empleado)) {
                return "redirect:/empleados";
            } else {
                model.addAttribute("errorMessage", "Ya existe un empleado con el mismo DNI");
                return "empleadosform";
            }
        } else {
            model.addAttribute("errorMessage", "Usuario no encontrado");
            return "redirect:/login";
        }
    }

    @GetMapping("/empleados/borrar")
    public String borrarEmpleado(@RequestParam(value = "id") Empleado empleado, Model model, Principal principal) {
        if (usuarioLogeado(model, principal)) {
            empleadoService.eliminarEmpleado(empleado);
            return "redirect:/empleados";
        } else {
            model.addAttribute("errorMessage", "Usuario no encontrado");
            return "redirect:/login";
        }
    }

    @GetMapping("/clientes")
    public String mostrarClientes(Model model, Principal principal) {
        if (usuarioLogeado(model, principal)) {
            List<Cliente> clientes = clienteService.listarClientes();
            model.addAttribute("clientes", clientes);
            return "clientes";
        } else {
            model.addAttribute("errorMessage", "Usuario no encontrado");
            return "redirect:/login";
        }
    }
    
    @PostMapping("/clientes/buscar")
    public String buscarCliente(String dni, Model model, Principal principal) {
        if (usuarioLogeado(model, principal)) {
            List<Cliente> clientes = clienteService.listarClientes().stream().filter(c -> c.getDocumento().contains(dni)).collect(Collectors.toList());
            model.addAttribute("clientes", clientes);
            return "clientes";
        } else {
            model.addAttribute("errorMessage", "Usuario no encontrado");
            return "redirect:/login";
        }
    }

    @GetMapping("/clientes/form")
    public String formCliente(@RequestParam(value = "id", required = false) Cliente cliente, Model model, Principal principal) {
        if (usuarioLogeado(model, principal)) {
            if (cliente != null) {
                model.addAttribute("editar", true);
            } else {
                cliente = new Cliente();
            }
            model.addAttribute("clases", claseService.listarClases());
            model.addAttribute("cliente", cliente);
            return "clientesform";
        } else {
            model.addAttribute("errorMessage", "Usuario no encontrado");
            return "redirect:/login";
        }
    }

    @PostMapping("/clientes/grabar")
    public String guardarCliente(@ModelAttribute("cliente") Cliente cliente, Model model, Principal principal) {
        if (usuarioLogeado(model, principal)) {
            if (clienteService.grabarCliente(cliente)) {
                return "redirect:/clientes";
            } else {
                model.addAttribute("clases", claseService.listarClases());
                model.addAttribute("errorMessage", "Ya existe un cliente con el mismo documento");
                return "clientesform";
            }
        } else {
            model.addAttribute("errorMessage", "Usuario no encontrado");
            return "redirect:/login";
        }
    }

    @GetMapping("/clientes/borrar")
    public String borrarCliente(@RequestParam(value = "id") Cliente cliente, Model model, Principal principal) {
        if (usuarioLogeado(model, principal)) {
            clienteService.eliminarCliente(cliente);
            return "redirect:/clientes";
        } else {
            model.addAttribute("errorMessage", "Usuario no encontrado");
            return "redirect:/login";
        }
    }

    @GetMapping("/servicios")
    public String mostrarServicios(Model model, Principal principal) {
        if (usuarioLogeado(model, principal)) {
            List<Servicio> servicios = servicioService.listarServicios();
            model.addAttribute("servicios", servicios);
            return "servicios";
        } else {
            model.addAttribute("errorMessage", "Usuario no encontrado");
            return "redirect:/login";
        }
    }

    @GetMapping("/servicios/form")
    public String formServicio(@RequestParam(value = "id", required = false) Servicio servicio, Model model, Principal principal) {
        if (usuarioLogeado(model, principal)) {
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

    @PostMapping("/servicios/grabar")
    public String guardarServicio(@ModelAttribute("servicio") Servicio servicio, Model model, Principal principal) {
        if (usuarioLogeado(model, principal)) {
            servicio.setUsuario(usuarioService.buscarPorUsuario(principal.getName()));
            if (servicioService.grabarServicio(servicio)) {
                return "redirect:/servicios";
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

    @GetMapping("/servicios/borrar")
    public String borrarServicio(@RequestParam(value = "id") Servicio servicio, Model model, Principal principal) {
        if (usuarioLogeado(model, principal)) {
            servicioService.eliminarServicio(servicio);
            return "redirect:/servicios";
        } else {
            model.addAttribute("errorMessage", "Usuario no encontrado");
            return "redirect:/login";
        }
    }

    /*-----------------------*/
    @GetMapping("/pedidos")
    public String mostrarPedidos(Model model, Principal principal) {
        if (usuarioLogeado(model, principal)) {
            model.addAttribute("pedidos", pedidoService.listarPedidos());
            return "pedidos";
        } else {
            model.addAttribute("errorMessage", "Usuario no encontrado");
            return "redirect:/login";
        }
    }

    @GetMapping("/pedidos/form")
    public String formPedido(Model model, Principal principal) {
        if (usuarioLogeado(model, principal)) {
            model.addAttribute("servicios", servicioService.listarServicios());
            return "pedidosform";
        } else {
            model.addAttribute("errorMessage", "Usuario no encontrado");
            return "redirect:/login";
        }
    }

    @PostMapping("/pedidos/cart")
    public String cartPedido(@RequestParam(value = "idServicio") int idServicio,
            @RequestParam(value = "cantidad") double cantidad,
            @RequestParam(value = "observacion") String observacion,
            Model model, Principal principal) {
        if (usuarioLogeado(model, principal)) {
            DetallePedido detalle = new DetallePedido();
            Servicio servicio = new Servicio();
            double sumaTotal = 0;

            servicio = servicioService.buscarServicioPorId(idServicio);

            detalle.setServicio(servicio);
            detalle.setCantidad(cantidad);
            detalle.setObservacion(observacion);
            detalle.setTotalUnitario(servicio.getPrecio() * cantidad);

            int idServicioAgregado = servicio.getIdServicio();
            boolean ingresado = detalles.stream().anyMatch(s -> s.getServicio().getIdServicio() == idServicioAgregado);

            if (!ingresado) {
                detalles.add(detalle);
            } else {
                model.addAttribute("errorMessage", "El servicio ya se registro");
            }

            sumaTotal = detalles.stream().mapToDouble(dt -> dt.getTotalUnitario()).sum();

            pedido.setSubtotal(sumaTotal);

            model.addAttribute("detalles", detalles);
            model.addAttribute("pedido", pedido);

            return "cart";
        } else {
            model.addAttribute("errorMessage", "Usuario no encontrado");
            return "redirect:/login";
        }
    }

    @GetMapping("/pedidos/cart/{id}")
    public String quitarServicioCart(@PathVariable int id, Model model, Principal principal) {
        if (usuarioLogeado(model, principal)) {
            List<DetallePedido> pedidosNuevo = new ArrayList<DetallePedido>();
            for (DetallePedido detallePedido : detalles) {
                if (detallePedido.getServicio().getIdServicio() != id) {
                    pedidosNuevo.add(detallePedido);
                }
            }

            detalles = pedidosNuevo;

            double sumaTotal = 0;
            sumaTotal = detalles.stream().mapToDouble(dt -> dt.getTotalUnitario()).sum();

            pedido.setSubtotal(sumaTotal);

            model.addAttribute("detalles", detalles);
            model.addAttribute("pedido", pedido);

            return "cart";
        } else {
            model.addAttribute("errorMessage", "Usuario no encontrado");
            return "redirect:/login";
        }

    }

    @GetMapping("pedidos/grabar")
    public String grabarPedido(Model model, Principal principal) {
        if (usuarioLogeado(model, principal)) {
            if (!detalles.isEmpty()) {
                pedido.setFecha(pedidoService.obtenerFecha());
                pedido.setCorrelativo(pedidoService.generarCorrelativo());

                /*setusuario, setempleado, setcliente*/
                pedido.setCliente(clienteService.buscarPorId(1));
                pedido.setEmpleado(empleadoService.buscarEmpleadoPorId(1));
                pedido.setUsuario(usuarioService.buscarPorId(1));
                pedido.setEstado(estadoService.buscarPorId(1));
                
                
                pedidoService.grabarPedido(pedido);

                for (DetallePedido dt : detalles) {
                    dt.setPedido(pedido);
                    detalleService.grabarDetalle(dt);
                }

                pedido = new Pedido();
                detalles.clear();
            }
            return "redirect:/pedidos";
        } else {
            model.addAttribute("errorMessage", "Usuario no encontrado");
            return "redirect:/login";
        }
    }

    /*posible implementacion*/
    @GetMapping("/pedidos/cart")
    public String mostrarCart(Model model, Principal principal) {
        if (usuarioLogeado(model, principal)) {
            model.addAttribute("detalles", detalles);
            model.addAttribute("pedido", pedido);

            return "cart";
        } else {
            model.addAttribute("errorMessage", "Usuario no encontrado");
            return "redirect:/login";
        }
    }

    @GetMapping("/pedidos/resumen")
    public String mostrarResumen(Model model, Principal principal) {
        if (usuarioLogeado(model, principal)) {

            model.addAttribute("detalles", detalles);
            model.addAttribute("pedido", pedido);
            return "pedidosresumen";
        } else {
            model.addAttribute("errorMessage", "Usuario no encontrado");
            return "redirect:/login";
        }
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
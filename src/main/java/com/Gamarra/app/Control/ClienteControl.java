package com.Gamarra.app.Control;

import com.Gamarra.app.Negocio.Cliente;
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
@RequestMapping("/clientes")
public class ClienteControl {

    private final AuthUtils authUtils;
    private final ClaseClienteService claseService;
    private final ClienteService clienteService;

    @GetMapping("/")
    public String mostrarClientes(Model model, Principal principal) {
        if (authUtils.usuarioLogeado(model, principal)) {
            return "clientes";
        } else {
            model.addAttribute("errorMessage", "Usuario no encontrado");
            return "redirect:/login";
        }
    }

    @GetMapping("/listar")
    public String listarClientes(Model model) {
        List<Cliente> clientes = clienteService.listarClientes();
        model.addAttribute("clientes", clientes);
        return "fragmentos/tablas :: tablaCliente";
    }

    @GetMapping("/form")
    public String formClientes(@RequestParam(value = "id", required = false) Integer idCliente, Model model) {
        Cliente cliente = null;
        if (idCliente != null) {
            cliente = clienteService.buscarPorId(idCliente);
        }
        if (cliente != null) {
            model.addAttribute("titulo", "Editar Cliente");
        } else {
            cliente = new Cliente();
            model.addAttribute("titulo", "Nuevo Cliente");
        }
        model.addAttribute("clases", claseService.listarClases());
        model.addAttribute("cliente", cliente);
        return "fragmentos/modals :: modalCliente";
    }

    @GetMapping("/buscar")
    public String buscarClientes(@RequestParam("documento") String documento, Model model) {
        List<Cliente> clientes = clienteService.buscarClientesPorDocumento(documento);
        model.addAttribute("clientes", clientes);
        return "fragmentos/tablas :: tablaCliente";
    }
    
    @PostMapping("/grabar")
    public String grabarCliente(@ModelAttribute("cliente") Cliente cliente, RedirectAttributes redirectAttributes) {
        if (clienteService.grabarCliente(cliente)) {
            redirectAttributes.addFlashAttribute("successMessage", "Cliente registrado exitosamente!!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "El Documento ingresado ya está registrado");
        }
        return "redirect:/clientes/";
    }

    @GetMapping("/borrar")
    public String borrarCliente(@RequestParam(value = "id") Cliente cliente, Model model, Principal principal) {
        if (authUtils.usuarioLogeado(model, principal)) {
            clienteService.eliminarCliente(cliente);
            return "redirect:/clientes/";
        } else {
            model.addAttribute("errorMessage", "Usuario no encontrado");
            return "redirect:/login";
        }
    }
}

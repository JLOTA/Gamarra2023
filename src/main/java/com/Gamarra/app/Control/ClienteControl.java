
package com.Gamarra.app.Control;

import com.Gamarra.app.Negocio.Cliente;
import com.Gamarra.app.Service.*;
import com.Gamarra.app.Utils.AuthUtils;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
            List<Cliente> clientes = clienteService.listarClientes();
            model.addAttribute("clientes", clientes);
            return "clientes";
        } else {
            model.addAttribute("errorMessage", "Usuario no encontrado");
            return "redirect:/login";
        }
    }

    @PostMapping("/buscar")
    public String buscarCliente(String dni, Model model, Principal principal) {
        if (authUtils.usuarioLogeado(model, principal)) {
            List<Cliente> clientes = clienteService.listarClientes().stream().filter(c -> c.getDocumento().contains(dni)).collect(Collectors.toList());
            model.addAttribute("clientes", clientes);
            return "clientes";
        } else {
            model.addAttribute("errorMessage", "Usuario no encontrado");
            return "redirect:/login";
        }
    }

    @GetMapping("/form")
    public String formCliente(@RequestParam(value = "id", required = false) Cliente cliente, Model model, Principal principal) {
        if (authUtils.usuarioLogeado(model, principal)) {
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

    @PostMapping("/grabar")
    public String guardarCliente(@ModelAttribute("cliente") Cliente cliente, Model model, Principal principal) {
        if (authUtils.usuarioLogeado(model, principal)) {
            if (clienteService.grabarCliente(cliente)) {
                return "redirect:/clientes/";
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

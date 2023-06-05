package com.Gamarra.app.Service;

import com.Gamarra.app.Negocio.Cliente;
import com.Gamarra.app.Persistencia.ClienteRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    @Autowired
    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public List<Cliente> listarClientes() {
        return clienteRepository.findAll();
    }
    
    public Cliente buscarPorDocumento(String documento){
        return clienteRepository.findByDocumento(documento);
    }
    
    public Cliente buscarPorId(int id){
        return clienteRepository.findById(id).orElse(null);
    }

    public boolean grabarCliente(Cliente cliente) {
        Cliente clienteExistente = this.buscarPorDocumento(cliente.getDocumento());
        if (clienteExistente != null && (clienteExistente.getIdCliente()!= cliente.getIdCliente())) {
            return false;
        } else {
            clienteRepository.save(cliente);
            return true;
        }
    }

    public String eliminarCliente(Cliente cliente) {
        clienteRepository.delete(cliente);
        String msg = "Cliente guardado: " + cliente.getDocumento();
        return msg;
    }
}

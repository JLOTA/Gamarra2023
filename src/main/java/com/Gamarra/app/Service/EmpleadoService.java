package com.Gamarra.app.Service;

import com.Gamarra.app.Negocio.Empleado;
import com.Gamarra.app.Persistencia.EmpleadoRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmpleadoService {

    private final EmpleadoRepository empleadoRepository;

    @Autowired
    public EmpleadoService(EmpleadoRepository empleadoRepository) {
        this.empleadoRepository = empleadoRepository;
    }

    public boolean grabarEmpleado(Empleado empleado) {
        Empleado empleadoExistente = this.buscarPorDni(empleado.getDni());
        if (empleadoExistente != null && (empleadoExistente.getIdEmpleado() != empleado.getIdEmpleado())) {
            return false;
        } else {
            empleadoRepository.save(empleado);
            return true;
        }
    }

    public Empleado buscarEmpleadoPorId(int id) {
        return empleadoRepository.findById(id).orElse(null);
    }

    public List<Empleado> listarEmpleados() {
        return empleadoRepository.findAll();
    }

    public String eliminarEmpleado(Empleado empleado) {
        empleadoRepository.delete(empleado);
        return "Empleado eliminado";
    }

    public Empleado buscarPorDni(String dni){
        return empleadoRepository.findByDni(dni);
    }
    
    public List<Empleado> buscarEmpleadosPorDni(String dni) {
        return empleadoRepository.findAllByDniContaining(dni);
    }
}

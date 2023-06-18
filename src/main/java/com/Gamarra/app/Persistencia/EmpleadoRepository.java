package com.Gamarra.app.Persistencia;

import com.Gamarra.app.Negocio.*;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, Integer> {

    List<Empleado> findAllByDniContaining(String dni);
    Empleado findByDni(String dni);
}

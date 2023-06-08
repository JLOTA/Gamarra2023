package com.Gamarra.app.Persistencia;

import com.Gamarra.app.Negocio.*;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnidadMedidaRepository extends JpaRepository<UnidadMedida, Integer> {
    
    Optional<UnidadMedida> findByNombreOrAbreviatura(String nombre, String abreviatura);
}

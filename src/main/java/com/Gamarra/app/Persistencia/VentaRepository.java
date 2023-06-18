
package com.Gamarra.app.Persistencia;
import com.Gamarra.app.Negocio.*;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface VentaRepository extends JpaRepository<Venta, Integer> {

    Page<Venta> findAllByOrderByFechaDesc(Pageable pageable);  
    Page<Venta> findAllByCorrelativoContainingOrderByFechaDesc(String correlativo, Pageable pageable);
    List<Venta> findByFechaBetween(LocalDate fechaInicio, LocalDate fechaFin);
}

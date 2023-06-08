
package com.Gamarra.app.Persistencia;
import com.Gamarra.app.Negocio.*;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {

    Page<Pedido> findAll(Pageable pageable);
    
    List<Pedido> findByFechaBetween(LocalDate fechaInicio, LocalDate fechaFin);

}

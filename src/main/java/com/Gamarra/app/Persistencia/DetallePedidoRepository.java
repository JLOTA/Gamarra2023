
package com.Gamarra.app.Persistencia;
import com.Gamarra.app.Negocio.*;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Integer> {

    List<DetallePedido> findByPedido(Pedido pedido);
}

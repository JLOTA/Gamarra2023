
package com.Gamarra.app.Persistencia;
import com.Gamarra.app.Negocio.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface EstadoRepository extends JpaRepository<Estado, Integer> {

}

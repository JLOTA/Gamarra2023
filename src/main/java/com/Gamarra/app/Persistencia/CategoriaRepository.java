
package com.Gamarra.app.Persistencia;
import com.Gamarra.app.Negocio.*;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {

    Optional<Categoria> findByNombreOrAbreviatura(String nombre, String abreviatura);

}

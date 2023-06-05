package com.Gamarra.app.Persistencia;

import com.Gamarra.app.Negocio.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Usuario findByUsuario(String usuario);
}

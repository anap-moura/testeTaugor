package com.taugor.testetaugor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.taugor.testetaugor.model.Usuario;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
}

package com.snkuni.sankuni.repositories;

import com.snkuni.sankuni.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {
    // Fundamental para cuando implementemos Spring Security y el Login
    Optional<Usuario> findByEmail(String email);
    boolean existsByEmail(String email);
}
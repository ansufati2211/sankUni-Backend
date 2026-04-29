package com.snkuni.sankuni.repositories;

import com.snkuni.sankuni.models.Docente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DocenteRepository extends JpaRepository<Docente, UUID> {
    Optional<Docente> findByUsuario_IdUsuario(UUID idUsuario);
}
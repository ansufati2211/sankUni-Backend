package com.snkuni.sankuni.repositories;

import com.snkuni.sankuni.models.Alumno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AlumnoRepository extends JpaRepository<Alumno, UUID> {
    // Permite encontrar el perfil de alumno usando el ID de su cuenta de usuario logueada
    Optional<Alumno> findByUsuario_IdUsuario(UUID idUsuario);
}
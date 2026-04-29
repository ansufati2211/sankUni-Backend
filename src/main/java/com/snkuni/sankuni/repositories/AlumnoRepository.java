package com.snkuni.sankuni.repositories;

import com.snkuni.sankuni.models.Alumno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface AlumnoRepository extends JpaRepository<Alumno, Long> {
    // Permite encontrar el perfil de alumno usando el ID de su cuenta de usuario logueada
    Optional<Alumno> findByUsuario_IdUsuario(Long idUsuario);
}
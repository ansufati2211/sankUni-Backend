package com.snkuni.sankuni.repositories;

import com.snkuni.sankuni.models.Alumno;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface AlumnoRepository extends JpaRepository<Alumno, Long> {
    Optional<Alumno> findByUsuario_IdUsuario(Long idUsuario);
    Optional<Alumno> findByUsuario_Dni(String dni); // NUEVO: Cruza la tabla Alumno con Usuario usando el DNI
    List<Alumno> findByCarrera_IdCarrera(Long idCarrera);
}
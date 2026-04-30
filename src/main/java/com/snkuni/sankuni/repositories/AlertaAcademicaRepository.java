package com.snkuni.sankuni.repositories;

import com.snkuni.sankuni.models.AlertaAcademica;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AlertaAcademicaRepository extends JpaRepository<AlertaAcademica, Long> {
    List<AlertaAcademica> findByResueltaFalse(); // Todas las alertas pendientes
    List<AlertaAcademica> findByDocente_IdDocenteAndResueltaFalse(Long idDocente); // Alertas de un profe
}
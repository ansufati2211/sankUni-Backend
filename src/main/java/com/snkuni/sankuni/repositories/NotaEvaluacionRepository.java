package com.snkuni.sankuni.repositories;

import com.snkuni.sankuni.models.NotaEvaluacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotaEvaluacionRepository extends JpaRepository<NotaEvaluacion, Long> {
    // Busca todas las notas de un examen específico
    List<NotaEvaluacion> findByEvaluacion_IdEvaluacion(Long idEvaluacion);
    
    Optional<NotaEvaluacion> findByEvaluacion_IdEvaluacionAndAlumno_IdAlumno(Long idEvaluacion, Long idAlumno);
}
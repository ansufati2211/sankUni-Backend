package com.snkuni.sankuni.repositories;

import com.snkuni.sankuni.models.EntregaEvaluacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntregaEvaluacionRepository extends JpaRepository<EntregaEvaluacion, Long> {
    boolean existsByEvaluacion_IdEvaluacionAndAlumno_IdAlumno(Long idEvaluacion, Long idAlumno);
    List<EntregaEvaluacion> findByEvaluacion_IdEvaluacion(Long idEvaluacion);
    List<EntregaEvaluacion> findByAlumno_IdAlumno(Long idAlumno);
}

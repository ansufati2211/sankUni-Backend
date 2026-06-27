package com.snkuni.sankuni.repositories;

import com.snkuni.sankuni.models.NotaEvaluacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotaEvaluacionRepository extends JpaRepository<NotaEvaluacion, Long> {

    List<NotaEvaluacion> findByEvaluacion_IdEvaluacion(Long idEvaluacion);

    Optional<NotaEvaluacion> findByEvaluacion_IdEvaluacionAndAlumno_IdAlumno(Long idEvaluacion, Long idAlumno);

    @Query("SELECT n FROM NotaEvaluacion n " +
           "JOIN FETCH n.evaluacion e " +
           "JOIN FETCH e.seccion s " +
           "JOIN FETCH s.curso c " +
           "WHERE n.alumno.idAlumno = :alumnoId " +
           "ORDER BY c.nombre, e.fechaExamen")
    List<NotaEvaluacion> findByAlumnoId(@Param("alumnoId") Long alumnoId);
}
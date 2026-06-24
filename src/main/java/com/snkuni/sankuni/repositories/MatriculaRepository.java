package com.snkuni.sankuni.repositories;

import com.snkuni.sankuni.models.Matricula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MatriculaRepository extends JpaRepository<Matricula, Long> {

    @Query(value = "SELECT fn_matricular_alumno(:p_alumno_id, :p_seccion_id, :p_monto)", nativeQuery = true)
    Long matricularAlumnoTransaccional(
            @Param("p_alumno_id") Long alumnoId,
            @Param("p_seccion_id") Long seccionId,
            @Param("p_monto") BigDecimal monto
    );

    List<Matricula> findByAlumno_IdAlumno(Long idAlumno);
    List<Matricula> findBySeccion_IdSeccion(Long idSeccion);

    @Query("SELECT m FROM Matricula m JOIN FETCH m.alumno a JOIN FETCH a.usuario u JOIN FETCH m.seccion s JOIN FETCH s.curso c WHERE m.fechaMatricula >= :desde ORDER BY m.fechaMatricula DESC")
    List<Matricula> findMatriculasRecientes(@Param("desde") LocalDateTime desde);
}
package com.snkuni.sankuni.repositories;

import com.snkuni.sankuni.models.Matricula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Repository
public interface MatriculaRepository extends JpaRepository<Matricula, UUID> {
    
    // Ejecuta la función de PostgreSQL que tú creaste
    @Query(value = "SELECT fn_matricular_alumno(:p_alumno_id, :p_seccion_id, :p_monto)", nativeQuery = true)
    UUID matricularAlumnoTransaccional(
            @Param("p_alumno_id") UUID alumnoId, 
            @Param("p_seccion_id") UUID seccionId, 
            @Param("p_monto") BigDecimal monto
    );

    List<Matricula> findByAlumno_IdAlumno(UUID idAlumno);
}
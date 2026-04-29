package com.snkuni.sankuni.repositories;

import com.snkuni.sankuni.models.Asistencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;



@Repository
public interface AsistenciaRepository extends JpaRepository<Asistencia, Long> {

    @Modifying
    @Query(value = "CALL sp_registrar_asistencia(:p_seccion_id, :p_alumno_id, :p_presente)", nativeQuery = true)
    void registrarAsistenciaMasiva(
            @Param("p_seccion_id") Long seccionId, 
            @Param("p_alumno_id") Long alumnoId, 
            @Param("p_presente") Boolean presente
    );
}
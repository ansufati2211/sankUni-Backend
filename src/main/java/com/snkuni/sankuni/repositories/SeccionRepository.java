package com.snkuni.sankuni.repositories;

import com.snkuni.sankuni.models.Seccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SeccionRepository extends JpaRepository<Seccion, Long> {
    List<Seccion> findByCicloAcademico(String cicloAcademico);
    List<Seccion> findByDocente_IdDocente(Long idDocente);
    List<Seccion> findByDocente_IdDocenteAndDiaSemana(Long idDocente, Integer diaSemana);
    
    // NUEVO: Buscar clases por Carrera y Ciclo (Para la auto-matrícula)
    @Query("SELECT s FROM Seccion s WHERE s.curso.carrera.idCarrera = :idCarrera AND s.cicloAcademico = :ciclo")
    List<Seccion> findByCarreraAndCiclo(@Param("idCarrera") Long idCarrera, @Param("ciclo") String ciclo);
}
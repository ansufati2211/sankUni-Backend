package com.snkuni.sankuni.repositories;

import com.snkuni.sankuni.models.Seccion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SeccionRepository extends JpaRepository<Seccion, Long> {
    List<Seccion> findByCicloAcademico(String cicloAcademico);
    List<Seccion> findByDocente_IdDocente(Long idDocente);
    
    // NUEVO: Para que el docente vea sus clases "del día de hoy"
    List<Seccion> findByDocente_IdDocenteAndDiaSemana(Long idDocente, Integer diaSemana); 
}
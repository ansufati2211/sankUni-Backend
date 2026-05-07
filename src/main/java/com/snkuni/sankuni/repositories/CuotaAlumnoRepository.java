package com.snkuni.sankuni.repositories;

import com.snkuni.sankuni.models.CuotaAlumno;
import com.snkuni.sankuni.models.enums.EstadoCuota;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CuotaAlumnoRepository extends JpaRepository<CuotaAlumno, Long> {
    // Para ver todas las cuotas (historial) de un alumno
    List<CuotaAlumno> findByAlumno_IdAlumno(Long idAlumno);
    
    // Para verificar si tiene deudas antes de matricularlo
    List<CuotaAlumno> findByAlumno_IdAlumnoAndEstado(Long idAlumno, EstadoCuota estado);
    
    List<CuotaAlumno> findByEstadoNot(EstadoCuota estado);
}
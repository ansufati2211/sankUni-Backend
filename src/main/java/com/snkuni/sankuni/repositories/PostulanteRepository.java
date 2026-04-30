package com.snkuni.sankuni.repositories;

import com.snkuni.sankuni.models.Postulante;
import com.snkuni.sankuni.models.enums.EstadoPostulante;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PostulanteRepository extends JpaRepository<Postulante, Long> {
    Optional<Postulante> findByDni(String dni);
    List<Postulante> findByEstado(EstadoPostulante estado); // Para el panel del Coordinador
}
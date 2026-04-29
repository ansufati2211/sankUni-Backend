package com.snkuni.sankuni.repositories;

import com.snkuni.sankuni.models.Seccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface SeccionRepository extends JpaRepository<Seccion, UUID> {
    List<Seccion> findByCicloAcademico(String cicloAcademico);
}
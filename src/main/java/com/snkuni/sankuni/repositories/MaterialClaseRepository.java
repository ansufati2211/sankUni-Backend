package com.snkuni.sankuni.repositories;

import com.snkuni.sankuni.models.MaterialClase;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MaterialClaseRepository extends JpaRepository<MaterialClase, Long> {
    List<MaterialClase> findBySeccion_IdSeccion(Long idSeccion);
}
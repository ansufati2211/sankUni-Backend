package com.snkuni.sankuni.repositories;

import com.snkuni.sankuni.models.AnuncioSeccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnuncioSeccionRepository extends JpaRepository<AnuncioSeccion, Long> {
    List<AnuncioSeccion> findBySeccion_IdSeccionOrderByFechaPublicacionDesc(Long idSeccion);
    List<AnuncioSeccion> findBySeccion_IdSeccionInOrderByFechaPublicacionDesc(List<Long> idsSeccion);
}

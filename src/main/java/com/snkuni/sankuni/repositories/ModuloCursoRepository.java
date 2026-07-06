package com.snkuni.sankuni.repositories;

import com.snkuni.sankuni.models.ModuloCurso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ModuloCursoRepository extends JpaRepository<ModuloCurso, Long> {
    List<ModuloCurso> findByCurso_IdCursoOrderByOrdenAsc(Long idCurso);
}

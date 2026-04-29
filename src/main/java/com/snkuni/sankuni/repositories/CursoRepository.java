package com.snkuni.sankuni.repositories;

import com.snkuni.sankuni.models.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {
    List<Curso> findByCarrera_IdCarrera(Long idCarrera);
}
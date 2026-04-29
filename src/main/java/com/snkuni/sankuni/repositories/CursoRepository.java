package com.snkuni.sankuni.repositories;

import com.snkuni.sankuni.models.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface CursoRepository extends JpaRepository<Curso, UUID> {
    List<Curso> findByCarrera_IdCarrera(UUID idCarrera);
}
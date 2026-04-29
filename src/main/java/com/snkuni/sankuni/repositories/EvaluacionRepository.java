package com.snkuni.sankuni.repositories;
import com.snkuni.sankuni.models.Evaluacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface EvaluacionRepository extends JpaRepository<Evaluacion, UUID> {
    List<Evaluacion> findBySeccion_IdSeccion(UUID idSeccion);
}
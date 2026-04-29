package com.snkuni.sankuni.repositories;
import com.snkuni.sankuni.models.NotaEvaluacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface NotaEvaluacionRepository extends JpaRepository<NotaEvaluacion, UUID> {
    List<NotaEvaluacion> findByEvaluacion_IdEvaluacion(UUID idEvaluacion);
}
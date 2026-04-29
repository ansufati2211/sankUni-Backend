package com.snkuni.sankuni.repositories;
import com.snkuni.sankuni.models.Evaluacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface EvaluacionRepository extends JpaRepository<Evaluacion, Long> {
    List<Evaluacion> findBySeccion_IdSeccion(Long idSeccion);
}
package com.snkuni.sankuni.repositories;

import com.snkuni.sankuni.models.Carrera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface CarreraRepository extends JpaRepository<Carrera, UUID> {
    boolean existsByNombre(String nombre);
}
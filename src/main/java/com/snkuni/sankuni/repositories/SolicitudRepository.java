package com.snkuni.sankuni.repositories;
import com.snkuni.sankuni.models.Solicitud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface SolicitudRepository extends JpaRepository<Solicitud, UUID> {
    List<Solicitud> findByEmisor_IdUsuario(UUID idUsuario);
}
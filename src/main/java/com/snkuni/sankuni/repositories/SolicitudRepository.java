package com.snkuni.sankuni.repositories;
import com.snkuni.sankuni.models.Solicitud;
import com.snkuni.sankuni.models.enums.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SolicitudRepository extends JpaRepository<Solicitud, Long> {
    List<Solicitud> findByEmisor_IdUsuario(Long idUsuario);
    
    // NUEVO: Para traer solo las solicitudes que el Admin debe revisar
    List<Solicitud> findByEstado(RequestStatus estado); 
}
package com.snkuni.sankuni.repositories;

import com.snkuni.sankuni.models.MensajeContacto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MensajeContactoRepository extends JpaRepository<MensajeContacto, Long> {
    List<MensajeContacto> findByEstadoAtencionFalse(); // Lista los no leídos
}
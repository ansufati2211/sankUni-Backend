package com.snkuni.sankuni.repositories;

import com.snkuni.sankuni.models.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PagoRepository extends JpaRepository<Pago, Long> {
    // REEMPLAZADO: Ahora busca por idCuota en vez de idMatricula
    List<Pago> findByCuota_IdCuota(Long idCuota);
}
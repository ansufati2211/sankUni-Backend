package com.snkuni.sankuni.services;

import com.snkuni.sankuni.dtos.PagoDTO;
import com.snkuni.sankuni.models.CuotaAlumno;
import com.snkuni.sankuni.models.Pago;
import com.snkuni.sankuni.models.enums.EstadoCuota;
import com.snkuni.sankuni.repositories.CuotaAlumnoRepository;
import com.snkuni.sankuni.repositories.PagoRepository;
import com.snkuni.sankuni.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PagoService {

    private final PagoRepository pagoRepository;
    private final CuotaAlumnoRepository cuotaRepository; // Inyectado para procesar pagos

    @Transactional(readOnly = true)
    public List<PagoDTO> listarTodosLosPagos() {
        return pagoRepository.findAll().stream()
                .map(this::mapearADto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PagoDTO> listarPorCuota(Long idCuota) {
        return pagoRepository.findByCuota_IdCuota(idCuota).stream()
                .map(this::mapearADto)
                .toList();
    }

    // EL NUEVO MOTOR DE LA PASARELA DE PAGOS
    @Transactional
    public PagoDTO procesarPago(Long idCuota, BigDecimal monto, String metodoPago) {
        CuotaAlumno cuota = cuotaRepository.findById(idCuota)
                .orElseThrow(() -> new ResourceNotFoundException("Cuota no encontrada"));
        
        Pago nuevoPago = Pago.builder()
                .cuota(cuota)
                .montoPagado(monto)
                .metodoPago(metodoPago)
                .build();
        pagoRepository.save(nuevoPago);

        cuota.setEstado(EstadoCuota.PAGADO);
        cuotaRepository.save(cuota);

        return mapearADto(nuevoPago);
    }

    private PagoDTO mapearADto(Pago pago) {
        return PagoDTO.builder()
                .idPago(pago.getIdPago())
                .idCuota(pago.getCuota().getIdCuota())
                .montoPagado(pago.getMontoPagado())
                .metodoPago(pago.getMetodoPago())
                .fechaPago(pago.getFechaPago())
                .build();
    }
}
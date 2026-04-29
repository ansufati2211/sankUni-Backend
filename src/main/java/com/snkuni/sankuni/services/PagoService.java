package com.snkuni.sankuni.services;

import com.snkuni.sankuni.dtos.PagoDTO;
import com.snkuni.sankuni.repositories.PagoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PagoService {

    private final PagoRepository pagoRepository;

    @Transactional(readOnly = true)
    public List<PagoDTO> listarTodosLosPagos() {
        return pagoRepository.findAll().stream()
                .map(p -> PagoDTO.builder()
                        .idPago(p.getIdPago())
                        .idMatricula(p.getMatricula().getIdMatricula())
                        .monto(p.getMonto())
                        .concepto(p.getConcepto())
                        .fechaPago(p.getFechaPago())
                        .build())
                .toList();
    }
}
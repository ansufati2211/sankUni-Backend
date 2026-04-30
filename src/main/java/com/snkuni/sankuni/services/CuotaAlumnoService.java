package com.snkuni.sankuni.services;

import com.snkuni.sankuni.dtos.CuotaAlumnoDTO;
import com.snkuni.sankuni.models.CuotaAlumno;
import com.snkuni.sankuni.repositories.CuotaAlumnoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CuotaAlumnoService {

    private final CuotaAlumnoRepository cuotaRepository;

    @Transactional(readOnly = true)
    public List<CuotaAlumnoDTO> listarPorAlumno(Long idAlumno) {
        return cuotaRepository.findByAlumno_IdAlumno(idAlumno).stream()
                .map(this::mapearADto)
                .toList();
    }

    private CuotaAlumnoDTO mapearADto(CuotaAlumno cuota) {
        return CuotaAlumnoDTO.builder()
                .idCuota(cuota.getIdCuota())
                .idAlumno(cuota.getAlumno().getIdAlumno())
                .cicloAcademico(cuota.getCicloAcademico())
                .mesCorrespondiente(cuota.getMesCorrespondiente())
                .montoTotal(cuota.getMontoTotal())
                .estado(cuota.getEstado().name())
                .fechaVencimiento(cuota.getFechaVencimiento())
                .build();
    }
}
package com.snkuni.sankuni.services;

import com.snkuni.sankuni.dtos.SeccionDTO;
import com.snkuni.sankuni.repositories.SeccionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SeccionService {

    private final SeccionRepository seccionRepository;

    @Transactional(readOnly = true)
    public List<SeccionDTO> listarPorCiclo(String cicloAcademico) {
        return seccionRepository.findByCicloAcademico(cicloAcademico).stream()
                .map(seccion -> SeccionDTO.builder()
                        .idSeccion(seccion.getIdSeccion())
                        .nombreCurso(seccion.getCurso().getNombre())
                        .nombreDocente(seccion.getDocente().getUsuario().getNombreCompleto())
                        .cicloAcademico(seccion.getCicloAcademico())
                        .diaSemana(seccion.getDiaSemana())
                        .horaInicio(seccion.getHoraInicio())
                        .horaFin(seccion.getHoraFin())
                        .build())
                .toList();
    }
}
package com.snkuni.sankuni.services;

import com.snkuni.sankuni.dtos.AlertaAcademicaDTO;
import com.snkuni.sankuni.repositories.AlertaAcademicaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlertaAcademicaService {
    private final AlertaAcademicaRepository alertaRepository;

    @Transactional(readOnly = true)
    public List<AlertaAcademicaDTO> listarPendientes() {
        return alertaRepository.findByResueltaFalse().stream()
                .map(a -> AlertaAcademicaDTO.builder()
                        .idAlerta(a.getIdAlerta()).tipo(a.getTipo().name())
                        .nombreSeccion(a.getSeccion().getCurso().getNombre())
                        .nombreDocente(a.getDocente().getUsuario().getNombreCompleto())
                        .mensaje(a.getMensaje()).resuelta(a.getResuelta())
                        .fechaCreacion(a.getFechaCreacion())
                        .build()).toList();
    }
}
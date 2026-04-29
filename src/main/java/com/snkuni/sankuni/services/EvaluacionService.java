package com.snkuni.sankuni.services;

import com.snkuni.sankuni.dtos.EvaluacionDTO;
import com.snkuni.sankuni.models.Evaluacion;
import com.snkuni.sankuni.models.Seccion;
import com.snkuni.sankuni.repositories.EvaluacionRepository;
import com.snkuni.sankuni.repositories.SeccionRepository;
import com.snkuni.sankuni.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import java.util.stream.Collectors;

@SuppressWarnings("unused")
@Service
@RequiredArgsConstructor
public class EvaluacionService {

    private final EvaluacionRepository evaluacionRepository;
    private final SeccionRepository seccionRepository;

    @Transactional
    public EvaluacionDTO crearEvaluacion(EvaluacionDTO dto) {
        Seccion seccion = seccionRepository.findById(dto.getSeccionId())
                .orElseThrow(() -> new ResourceNotFoundException("Sección no encontrada"));

        Evaluacion evaluacion = Evaluacion.builder()
                .seccion(seccion)
                .nombreExamen(dto.getNombreExamen())
                .pesoPorcentaje(dto.getPesoPorcentaje())
                .fechaExamen(dto.getFechaExamen())
                .build();

        Evaluacion guardada = evaluacionRepository.save(evaluacion);
        dto.setIdEvaluacion(guardada.getIdEvaluacion());
        return dto;
    }

    @Transactional(readOnly = true)
    public List<EvaluacionDTO> listarPorSeccion(Long idSeccion) {
        return evaluacionRepository.findBySeccion_IdSeccion(idSeccion).stream()
                .map(e -> EvaluacionDTO.builder()
                        .idEvaluacion(e.getIdEvaluacion())
                        .seccionId(e.getSeccion().getIdSeccion())
                        .nombreExamen(e.getNombreExamen())
                        .pesoPorcentaje(e.getPesoPorcentaje())
                        .fechaExamen(e.getFechaExamen())
                        .fechaPublicacionNotas(e.getFechaPublicacionNotas())
                        .build())
                .toList();
    }
}
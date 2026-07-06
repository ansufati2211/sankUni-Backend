package com.snkuni.sankuni.services;

import com.snkuni.sankuni.dtos.EvaluacionDTO;
import com.snkuni.sankuni.exceptions.BusinessLogicException;
import com.snkuni.sankuni.models.Evaluacion;
import com.snkuni.sankuni.models.ModuloCurso;
import com.snkuni.sankuni.models.Seccion;
import com.snkuni.sankuni.repositories.EvaluacionRepository;
import com.snkuni.sankuni.repositories.ModuloCursoRepository;
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
    private final ModuloCursoRepository moduloRepository;

    @Transactional
    public EvaluacionDTO crearEvaluacion(EvaluacionDTO dto) {
        Seccion seccion = seccionRepository.findById(dto.getSeccionId())
                .orElseThrow(() -> new ResourceNotFoundException("Sección no encontrada"));

        ModuloCurso modulo = resolverModulo(dto.getIdModulo(), seccion);

        Evaluacion evaluacion = Evaluacion.builder()
                .seccion(seccion)
                .modulo(modulo)
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
                .map(this::mapearADto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<EvaluacionDTO> listarPorModulo(Long idModulo) {
        return evaluacionRepository.findByModulo_IdModulo(idModulo).stream()
                .map(this::mapearADto)
                .toList();
    }

    private ModuloCurso resolverModulo(Long idModulo, Seccion seccion) {
        if (idModulo == null) {
            return null;
        }
        ModuloCurso modulo = moduloRepository.findById(idModulo)
                .orElseThrow(() -> new ResourceNotFoundException("Módulo no encontrado"));
        if (!modulo.getCurso().getIdCurso().equals(seccion.getCurso().getIdCurso())) {
            throw new BusinessLogicException("El módulo indicado no pertenece al curso de la sección seleccionada.");
        }
        return modulo;
    }

    private EvaluacionDTO mapearADto(Evaluacion e) {
        return EvaluacionDTO.builder()
                .idEvaluacion(e.getIdEvaluacion())
                .seccionId(e.getSeccion().getIdSeccion())
                .idModulo(e.getModulo() != null ? e.getModulo().getIdModulo() : null)
                .nombreExamen(e.getNombreExamen())
                .pesoPorcentaje(e.getPesoPorcentaje())
                .fechaExamen(e.getFechaExamen())
                .fechaPublicacionNotas(e.getFechaPublicacionNotas())
                .build();
    }
}
package com.snkuni.sankuni.services;

import com.snkuni.sankuni.dtos.NotaEvaluacionDTO;
import com.snkuni.sankuni.models.Alumno;
import com.snkuni.sankuni.models.Evaluacion;
import com.snkuni.sankuni.models.NotaEvaluacion;
import com.snkuni.sankuni.repositories.AlumnoRepository;
import com.snkuni.sankuni.repositories.EvaluacionRepository;
import com.snkuni.sankuni.repositories.NotaEvaluacionRepository;
import com.snkuni.sankuni.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotaEvaluacionService {

    private final NotaEvaluacionRepository notaRepository;
    private final EvaluacionRepository evaluacionRepository;
    private final AlumnoRepository alumnoRepository;

    @Transactional
    public NotaEvaluacionDTO registrarNota(NotaEvaluacionDTO dto) {
        Evaluacion evaluacion = evaluacionRepository.findById(dto.getEvaluacionId())
                .orElseThrow(() -> new ResourceNotFoundException("Evaluación no encontrada"));
                
        Alumno alumno = alumnoRepository.findById(dto.getAlumnoId())
                .orElseThrow(() -> new ResourceNotFoundException("Alumno no encontrado"));

        // Buscamos si la nota ya existe
        NotaEvaluacion nota = notaRepository.findByEvaluacion_IdEvaluacionAndAlumno_IdAlumno(evaluacion.getIdEvaluacion(), alumno.getIdAlumno())
                .orElse(null);

        // Si no existe, creamos el molde vacío
        if (nota == null) {
            nota = NotaEvaluacion.builder()
                    .evaluacion(evaluacion)
                    .alumno(alumno)
                    .build();
        }

        // Le asignamos el valor de la nota
        nota.setNota(dto.getNota());
        nota.setFechaRegistro(LocalDateTime.now());
        
        NotaEvaluacion guardada = notaRepository.save(nota);
        
        dto.setIdNota(guardada.getIdNota());
        dto.setNombreAlumno(alumno.getUsuario().getNombreCompleto());
        dto.setFechaRegistro(guardada.getFechaRegistro());
        
        return dto;
    }

    @Transactional(readOnly = true)
    public List<NotaEvaluacionDTO> obtenerNotasPorEvaluacion(Long idEvaluacion) {
        return notaRepository.findByEvaluacion_IdEvaluacion(idEvaluacion).stream()
                .map(n -> NotaEvaluacionDTO.builder()
                        .idNota(n.getIdNota())
                        .evaluacionId(n.getEvaluacion().getIdEvaluacion())
                        .alumnoId(n.getAlumno().getIdAlumno())
                        .nombreAlumno(n.getAlumno().getUsuario().getNombreCompleto())
                        .nota(n.getNota())
                        .fechaRegistro(n.getFechaRegistro())
                        .build())
                .toList();
    }
}
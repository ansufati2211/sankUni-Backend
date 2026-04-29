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

        NotaEvaluacion nota = NotaEvaluacion.builder()
                .evaluacion(evaluacion)
                .alumno(alumno)
                .nota(dto.getNota())
                .build();

        NotaEvaluacion guardada = notaRepository.save(nota);
        dto.setIdNota(guardada.getIdNota());
        dto.setNombreAlumno(alumno.getUsuario().getNombreCompleto());
        dto.setFechaRegistro(LocalDateTime.now());
        
        return dto;
    }
}
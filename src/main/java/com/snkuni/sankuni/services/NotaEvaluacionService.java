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

        NotaEvaluacion nota = notaRepository.findByEvaluacion_IdEvaluacionAndAlumno_IdAlumno(evaluacion.getIdEvaluacion(), alumno.getIdAlumno())
                .orElse(null);

        if (nota == null) {
            nota = NotaEvaluacion.builder()
                    .evaluacion(evaluacion)
                    .alumno(alumno)
                    .build();
        }

        nota.setNota(dto.getNota());
        nota.setComentario(dto.getComentario());
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
                        .comentario(n.getComentario())
                        .fechaRegistro(n.getFechaRegistro())
                        .build())
                .toList();
    }

    @Transactional(readOnly = true)
    public List<NotaEvaluacionDTO> obtenerNotasPorAlumno(Long alumnoId) {
        return notaRepository.findByAlumnoId(alumnoId).stream()
                .map(n -> {
                    var ev = n.getEvaluacion();
                    var sec = (ev != null) ? ev.getSeccion() : null;
                    String nombreCurso = (sec != null && sec.getCurso() != null) ? sec.getCurso().getNombre() : "Curso Desconocido";
                    
                    return NotaEvaluacionDTO.builder()
                            .idNota(n.getIdNota())
                            .evaluacionId(ev != null ? ev.getIdEvaluacion() : null)
                            .alumnoId(n.getAlumno().getIdAlumno())
                            .nota(n.getNota())
                            .comentario(n.getComentario())
                            .fechaRegistro(n.getFechaRegistro())
                            .nombreExamen(ev != null ? ev.getNombreExamen() : "Sin Nombre")
                            .pesoPorcentaje(ev != null ? ev.getPesoPorcentaje() : 0)
                            .fechaExamen(ev != null ? ev.getFechaExamen() : null)
                            .nombreCurso(nombreCurso)
                            .seccionId(sec != null ? sec.getIdSeccion() : null)
                            .cicloAcademico(sec != null ? sec.getCicloAcademico() : "Desconocido")
                            .build();
                })
                .toList();
    }
}
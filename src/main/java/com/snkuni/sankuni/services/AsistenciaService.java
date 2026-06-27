package com.snkuni.sankuni.services;

import com.snkuni.sankuni.dtos.AsistenciaDTO;
import com.snkuni.sankuni.dtos.AsistenciaRequestDTO;
import com.snkuni.sankuni.exceptions.BusinessLogicException;
import com.snkuni.sankuni.repositories.AsistenciaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AsistenciaService {

    private final AsistenciaRepository asistenciaRepository;

    @Transactional
    public void registrarAsistencia(AsistenciaRequestDTO request) {
        try {
            asistenciaRepository.registrarAsistenciaMasiva(
                    request.getSeccionId(), request.getAlumnoId(), request.getPresente()
            );
        } catch (Exception ex) {
            throw new BusinessLogicException("No se pudo registrar la asistencia: " + ex.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public List<AsistenciaDTO> obtenerPorSeccionYFecha(Long idSeccion, LocalDate fecha) {
        return asistenciaRepository.findBySeccion_IdSeccionAndFecha(idSeccion, fecha).stream()
                .map(a -> AsistenciaDTO.builder()
                        .alumnoId(a.getAlumno().getIdAlumno())
                        .presente(a.getPresente())
                        .build())
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AsistenciaDTO> obtenerPorAlumno(Long alumnoId) {
        return asistenciaRepository.findByAlumnoId(alumnoId).stream()
                .map(a -> {
                    var sec = a.getSeccion();
                    String nombreCurso = (sec != null && sec.getCurso() != null) ? sec.getCurso().getNombre() : "Curso Desconocido";
                    
                    return AsistenciaDTO.builder()
                            .alumnoId(alumnoId)
                            .presente(a.getPresente())
                            .fecha(a.getFecha())
                            .nombreCurso(nombreCurso)
                            .seccionId(sec != null ? sec.getIdSeccion() : null)
                            .cicloAcademico(sec != null ? sec.getCicloAcademico() : "Desconocido")
                            .build();
                })
                .toList();
    }
}
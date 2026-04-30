package com.snkuni.sankuni.services;

import com.snkuni.sankuni.dtos.SeccionDTO;
import com.snkuni.sankuni.models.Curso;
import com.snkuni.sankuni.models.Docente;
import com.snkuni.sankuni.models.Seccion;
import com.snkuni.sankuni.models.enums.ModalidadSeccion;
import com.snkuni.sankuni.repositories.CursoRepository;
import com.snkuni.sankuni.repositories.DocenteRepository;
import com.snkuni.sankuni.repositories.SeccionRepository;
import com.snkuni.sankuni.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SeccionService {

    private final SeccionRepository seccionRepository;
    private final CursoRepository cursoRepository;
    private final DocenteRepository docenteRepository;

    @Transactional(readOnly = true)
    public List<SeccionDTO> listarTodas() {
        return seccionRepository.findAll().stream().map(this::mapearADto).toList();
    }

    @Transactional(readOnly = true)
    public List<SeccionDTO> listarPorCiclo(String cicloAcademico) {
        return seccionRepository.findByCicloAcademico(cicloAcademico).stream().map(this::mapearADto).toList();
    }

    // NUEVO: Mis Cursos (Docente)
    @Transactional(readOnly = true)
    public List<SeccionDTO> listarPorDocente(Long idDocente) {
        return seccionRepository.findByDocente_IdDocente(idDocente).stream().map(this::mapearADto).toList();
    }

    // NUEVO: Abrir Curso (Coordinador)
    @Transactional
    public SeccionDTO crearSeccion(SeccionDTO dto) {
        Curso curso = cursoRepository.findById(dto.getCursoId())
                .orElseThrow(() -> new ResourceNotFoundException("Curso no encontrado"));
        Docente docente = docenteRepository.findById(dto.getDocenteId())
                .orElseThrow(() -> new ResourceNotFoundException("Docente no encontrado"));

        Seccion seccion = Seccion.builder()
                .curso(curso)
                .docente(docente)
                .cicloAcademico(dto.getCicloAcademico())
                .diaSemana(dto.getDiaSemana())
                .horaInicio(dto.getHoraInicio())
                .horaFin(dto.getHoraFin())
                .modalidad(dto.getModalidad() != null ? ModalidadSeccion.valueOf(dto.getModalidad().toUpperCase()) : ModalidadSeccion.PRESENCIAL)
                .build();

        Seccion guardada = seccionRepository.save(seccion);
        return mapearADto(guardada);
    }
// NUEVO: Clases del día de hoy (Docente)
    @Transactional(readOnly = true)
    public List<SeccionDTO> listarPorDocenteYDia(Long idDocente, Integer diaSemana) {
        return seccionRepository.findByDocente_IdDocenteAndDiaSemana(idDocente, diaSemana).stream()
                .map(this::mapearADto).toList();
    }

    // NUEVO: Editar programación de curso (Coordinador)
    @Transactional
    public SeccionDTO editarSeccion(Long idSeccion, SeccionDTO dto) {
        Seccion seccion = seccionRepository.findById(idSeccion)
                .orElseThrow(() -> new ResourceNotFoundException("Sección no encontrada"));

        if (dto.getCursoId() != null) {
            seccion.setCurso(cursoRepository.findById(dto.getCursoId()).orElseThrow());
        }
        if (dto.getDocenteId() != null) {
            seccion.setDocente(docenteRepository.findById(dto.getDocenteId()).orElseThrow());
        }
        if (dto.getCicloAcademico() != null) seccion.setCicloAcademico(dto.getCicloAcademico());
        if (dto.getDiaSemana() != null) seccion.setDiaSemana(dto.getDiaSemana());
        if (dto.getHoraInicio() != null) seccion.setHoraInicio(dto.getHoraInicio());
        if (dto.getHoraFin() != null) seccion.setHoraFin(dto.getHoraFin());
        if (dto.getModalidad() != null) seccion.setModalidad(com.snkuni.sankuni.models.enums.ModalidadSeccion.valueOf(dto.getModalidad().toUpperCase()));

        return mapearADto(seccionRepository.save(seccion));
    }
    private SeccionDTO mapearADto(Seccion seccion) {
        return SeccionDTO.builder()
                .idSeccion(seccion.getIdSeccion())
                .cursoId(seccion.getCurso().getIdCurso())
                .docenteId(seccion.getDocente().getIdDocente())
                .nombreCurso(seccion.getCurso().getNombre())
                .nombreDocente(seccion.getDocente().getUsuario().getNombreCompleto()) 
                .cicloAcademico(seccion.getCicloAcademico())
                .diaSemana(seccion.getDiaSemana())
                .horaInicio(seccion.getHoraInicio())
                .horaFin(seccion.getHoraFin())
                .modalidad(seccion.getModalidad() != null ? seccion.getModalidad().name() : "PRESENCIAL")
                .build();
    }
}
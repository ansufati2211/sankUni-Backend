package com.snkuni.sankuni.services;

import com.snkuni.sankuni.dtos.SeccionDTO;
import com.snkuni.sankuni.models.*;
import com.snkuni.sankuni.models.enums.ModalidadSeccion;
import com.snkuni.sankuni.repositories.*;
import com.snkuni.sankuni.exceptions.ResourceNotFoundException;
import com.snkuni.sankuni.exceptions.BusinessLogicException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SeccionService {
    private final SeccionRepository seccionRepository;
    private final CursoRepository cursoRepository;
    private final DocenteRepository docenteRepository;

    @Transactional
    public SeccionDTO crearSeccion(SeccionDTO dto) {
        LocalTime[] horarios = validarYParsearHorarios(dto.getHoraInicio(), dto.getHoraFin());

        Curso curso = cursoRepository.findById(dto.getCursoId())
                .orElseThrow(() -> new ResourceNotFoundException("Curso no encontrado"));
        Docente docente = docenteRepository.findById(dto.getDocenteId())
                .orElseThrow(() -> new ResourceNotFoundException("Docente no encontrado"));

        Seccion seccion = Seccion.builder()
                .curso(curso).docente(docente)
                .cicloAcademico(dto.getCicloAcademico())
                .diaSemana(dto.getDiaSemana())
                .horaInicio(horarios[0]) 
                .horaFin(horarios[1])    
                .modalidad(dto.getModalidad() != null ? ModalidadSeccion.valueOf(dto.getModalidad().toUpperCase()) : ModalidadSeccion.PRESENCIAL)
                .build();

        return mapearADto(seccionRepository.save(seccion));
    }

    @Transactional
    public SeccionDTO editarSeccion(Long id, SeccionDTO dto) {
        Seccion seccion = seccionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sección no encontrada"));

        LocalTime[] horarios = validarYParsearHorarios(dto.getHoraInicio(), dto.getHoraFin());

        seccion.setCicloAcademico(dto.getCicloAcademico());
        seccion.setDiaSemana(dto.getDiaSemana());
        seccion.setHoraInicio(horarios[0]);
        seccion.setHoraFin(horarios[1]);
        seccion.setModalidad(ModalidadSeccion.valueOf(dto.getModalidad().toUpperCase()));

        if (!seccion.getCurso().getIdCurso().equals(dto.getCursoId())) {
            seccion.setCurso(cursoRepository.findById(dto.getCursoId()).orElseThrow());
        }
        if (!seccion.getDocente().getIdDocente().equals(dto.getDocenteId())) {
            seccion.setDocente(docenteRepository.findById(dto.getDocenteId()).orElseThrow());
        }

        return mapearADto(seccionRepository.save(seccion));
    }

    private LocalTime[] validarYParsearHorarios(String hInicioStr, String hFinStr) {
        try {
            String inicioFormat = hInicioStr.length() == 5 ? hInicioStr + ":00" : hInicioStr;
            String finFormat = hFinStr.length() == 5 ? hFinStr + ":00" : hFinStr;

            LocalTime inicio = LocalTime.parse(inicioFormat);
            LocalTime fin = LocalTime.parse(finFormat);

            if (!inicio.isBefore(fin)) {
                throw new BusinessLogicException("La hora de inicio (" + inicioFormat + ") debe ser anterior a la hora de fin (" + finFormat + ").");
            }
            return new LocalTime[]{inicio, fin};
            
        } catch (DateTimeParseException e) {
            throw new BusinessLogicException("El formato de la hora es incorrecto. Debe ser HH:mm");
        }
    }

    @Transactional(readOnly = true)
    public List<SeccionDTO> listarTodas() {
        return seccionRepository.findAll().stream().map(this::mapearADto).toList();
    }

    // MÉTODOS RESTAURADOS
    @Transactional(readOnly = true)
    public List<SeccionDTO> listarPorCiclo(String cicloAcademico) {
        return seccionRepository.findByCicloAcademico(cicloAcademico).stream().map(this::mapearADto).toList();
    }

    @Transactional(readOnly = true)
    public List<SeccionDTO> listarPorDocente(Long idDocente) {
        return seccionRepository.findByDocente_IdDocente(idDocente).stream().map(this::mapearADto).toList();
    }

    @Transactional(readOnly = true)
    public List<SeccionDTO> listarPorDocenteYDia(Long idDocente, Integer diaSemana) {
        return seccionRepository.findByDocente_IdDocenteAndDiaSemana(idDocente, diaSemana).stream()
                .map(this::mapearADto).toList();
    }

    private SeccionDTO mapearADto(Seccion s) {
        return SeccionDTO.builder()
                .idSeccion(s.getIdSeccion())
                .cursoId(s.getCurso().getIdCurso())
                .docenteId(s.getDocente().getIdDocente())
                .nombreCurso(s.getCurso().getNombre())
                .nombreDocente(s.getDocente().getUsuario().getNombreCompleto())
                .cicloAcademico(s.getCicloAcademico())
                .diaSemana(s.getDiaSemana())
                .horaInicio(s.getHoraInicio().toString()) 
                .horaFin(s.getHoraFin().toString())       
                .modalidad(s.getModalidad().name())
                .build();
    }
}
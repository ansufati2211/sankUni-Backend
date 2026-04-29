package com.snkuni.sankuni.services;

import com.snkuni.sankuni.dtos.AlumnoDTO;
import com.snkuni.sankuni.dtos.UsuarioDTO;
import com.snkuni.sankuni.models.Alumno;
import com.snkuni.sankuni.repositories.AlumnoRepository;
import com.snkuni.sankuni.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlumnoService {

    private final AlumnoRepository alumnoRepository;

    @Transactional(readOnly = true)
    public List<AlumnoDTO> listarTodos() {
        return alumnoRepository.findAll().stream()
                .map(this::mapearAAlumnoDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public AlumnoDTO obtenerPorId(Long id) {
        return alumnoRepository.findById(id)
                .map(this::mapearAAlumnoDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Alumno no encontrado con ID: " + id));
    }

    @Transactional(readOnly = true)
    public AlumnoDTO obtenerPerfilPorUsuario(Long idUsuario) {
        return alumnoRepository.findByUsuario_IdUsuario(idUsuario)
                .map(this::mapearAAlumnoDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Perfil de alumno no encontrado"));
    }

    // Centralizamos el mapeo para evitar errores de tipos
    private AlumnoDTO mapearAAlumnoDTO(Alumno alumno) {
        return AlumnoDTO.builder()
                .idAlumno(alumno.getIdAlumno())
                .usuario(UsuarioDTO.builder()
                        .idUsuario(alumno.getUsuario().getIdUsuario())
                        .nombreCompleto(alumno.getUsuario().getNombreCompleto())
                        .email(alumno.getUsuario().getEmail())
                        .rol(alumno.getUsuario().getRol().name())
                        .build())
                .nombreCarrera(alumno.getCarrera().getNombre())
                .estado(alumno.getEstado().name())
                .fechaIngreso(alumno.getFechaIngreso())
                .build();
    }
}
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
        return alumnoRepository.findAll().stream().map(this::mapearAAlumnoDTO).toList();
    }

    @Transactional(readOnly = true)
    public AlumnoDTO obtenerPorId(Long id) {
        return alumnoRepository.findById(id).map(this::mapearAAlumnoDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Alumno no encontrado"));
    }

    @Transactional(readOnly = true)
    public AlumnoDTO obtenerPerfilPorUsuario(Long idUsuario) {
        return alumnoRepository.findByUsuario_IdUsuario(idUsuario).map(this::mapearAAlumnoDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Perfil no encontrado"));
    }

    // NUEVO: Buscar Alumno por DNI para el Coordinador
    @Transactional(readOnly = true)
    public AlumnoDTO buscarPorDni(String dni) {
        return alumnoRepository.findByUsuario_Dni(dni).map(this::mapearAAlumnoDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Alumno con DNI " + dni + " no encontrado"));
    }

    private AlumnoDTO mapearAAlumnoDTO(Alumno alumno) {
        return AlumnoDTO.builder()
                .idAlumno(alumno.getIdAlumno())
                .usuario(UsuarioDTO.builder()
                        .idUsuario(alumno.getUsuario().getIdUsuario())
                        .dni(alumno.getUsuario().getDni())
                        .nombres(alumno.getUsuario().getNombres())
                        .apellidos(alumno.getUsuario().getApellidos())
                        .nombreCompleto(alumno.getUsuario().getNombreCompleto())
                        .email(alumno.getUsuario().getEmail())
                        .rol(alumno.getUsuario().getRol().name())
                        .build())
                .nombreCarrera(alumno.getCarrera().getNombre())
                .estado(alumno.getEstado().name())
                .fechaIngreso(alumno.getFechaIngreso())
                .promedioHistorico(alumno.getPromedioHistorico())
                .build();
    }
}
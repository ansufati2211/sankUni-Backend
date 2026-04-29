package com.snkuni.sankuni.services;

import com.snkuni.sankuni.dtos.AlumnoDTO;
import com.snkuni.sankuni.dtos.UsuarioDTO;
import com.snkuni.sankuni.repositories.AlumnoRepository;
import com.snkuni.sankuni.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@RequiredArgsConstructor
public class AlumnoService {

    private final AlumnoRepository alumnoRepository;

    @Transactional(readOnly = true)
    public AlumnoDTO obtenerPerfilPorUsuario(Long idUsuario) {
        return alumnoRepository.findByUsuario_IdUsuario(idUsuario)
                .map(alumno -> AlumnoDTO.builder()
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
                        .build())
                .orElseThrow(() -> new ResourceNotFoundException("Perfil de alumno no encontrado"));
    }
}
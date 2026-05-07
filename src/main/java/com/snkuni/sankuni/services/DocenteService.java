package com.snkuni.sankuni.services;

import com.snkuni.sankuni.dtos.DocenteDTO;
import com.snkuni.sankuni.dtos.PostulanteDTO;
import com.snkuni.sankuni.dtos.UsuarioDTO;
import com.snkuni.sankuni.models.Docente;
import com.snkuni.sankuni.models.Usuario;
import com.snkuni.sankuni.models.enums.UserRole;
import com.snkuni.sankuni.models.enums.TeacherStatus;
import com.snkuni.sankuni.repositories.DocenteRepository;
import com.snkuni.sankuni.repositories.UsuarioRepository;
import com.snkuni.sankuni.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DocenteService {

    private final DocenteRepository docenteRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public DocenteDTO registrarDocente(PostulanteDTO dto, String especialidad) {
        // 1. Crear el objeto Usuario con el rol DOCENTE
        Usuario u = Usuario.builder()
                .dni(dto.getDni())
                .nombres(dto.getNombres())
                .apellidos(dto.getApellidos())
                .email(dto.getCorreo())
                // Se usa el DNI como contraseña inicial por defecto
                .passwordHash(passwordEncoder.encode(dto.getDni()))
                .rol(UserRole.DOCENTE)
                .build();
        
        Usuario usuarioGuardado = usuarioRepository.save(u);

        // 2. Crear el perfil de Docente vinculado al usuario recién creado
        Docente d = Docente.builder()
                .usuario(usuarioGuardado)
                .especialidad(especialidad)
                .estado(TeacherStatus.ACTIVO)
                .build();
        
        Docente docenteGuardado = docenteRepository.save(d);

        return mapearADto(docenteGuardado);
    }

    @Transactional(readOnly = true)
    public List<DocenteDTO> listarTodos() {
        return docenteRepository.findAll().stream()
                .map(this::mapearADto)
                .toList();
    }

    private DocenteDTO mapearADto(Docente docente) {
        return DocenteDTO.builder()
                .idDocente(docente.getIdDocente())
                .usuario(UsuarioDTO.builder()
                        .idUsuario(docente.getUsuario().getIdUsuario())
                        .dni(docente.getUsuario().getDni())
                        .nombres(docente.getUsuario().getNombres())
                        .apellidos(docente.getUsuario().getApellidos())
                        .nombreCompleto(docente.getUsuario().getNombreCompleto())
                        .email(docente.getUsuario().getEmail())
                        .rol(docente.getUsuario().getRol().name())
                        .build())
                .especialidad(docente.getEspecialidad())
                .estado(docente.getEstado().name())
                .build();
    }

    @Transactional(readOnly = true)
    public DocenteDTO obtenerPorId(Long id) {
        return docenteRepository.findById(id)
                .map(this::mapearADto)
                .orElseThrow(() -> new ResourceNotFoundException("Docente no encontrado con ID: " + id));
    }

    @Transactional(readOnly = true)
    public DocenteDTO obtenerPerfilPorUsuario(Long usuarioId) {
        return docenteRepository.findByUsuario_IdUsuario(usuarioId)
                .map(this::mapearADto)
                .orElseThrow(() -> new ResourceNotFoundException("Perfil de docente no encontrado"));
    }
}
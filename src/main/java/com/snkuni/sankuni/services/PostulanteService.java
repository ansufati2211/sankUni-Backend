package com.snkuni.sankuni.services;

import com.snkuni.sankuni.dtos.PostulanteDTO;
import com.snkuni.sankuni.models.*;
import com.snkuni.sankuni.models.enums.*;
import com.snkuni.sankuni.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostulanteService {

    private final PostulanteRepository postulanteRepository;
    private final CarreraRepository carreraRepository;
    private final UsuarioRepository usuarioRepository;
    private final AlumnoRepository alumnoRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public PostulanteDTO registrar(PostulanteDTO dto) {
        Carrera carrera = carreraRepository.findById(dto.getCarreraId()).orElseThrow();
        Postulante p = Postulante.builder()
                .dni(dto.getDni()).nombres(dto.getNombres()).apellidos(dto.getApellidos())
                .correo(dto.getCorreo()).carrera(carrera).sede(dto.getSede()).turno(dto.getTurno())
                .build();
        p = postulanteRepository.save(p);
        dto.setIdPostulante(p.getIdPostulante());
        return dto;
    }

    @Transactional(readOnly = true)
    public List<PostulanteDTO> listarPendientes() {
        return postulanteRepository.findByEstado(EstadoPostulante.EN_REVISION).stream()
                .map(p -> PostulanteDTO.builder()
                        .idPostulante(p.getIdPostulante()).dni(p.getDni())
                        .nombres(p.getNombres()).apellidos(p.getApellidos())
                        .correo(p.getCorreo()).nombreCarrera(p.getCarrera().getNombre())
                        .sede(p.getSede()).turno(p.getTurno()).estado(p.getEstado().name())
                        .build()).toList();
    }

    @Transactional
    public String aprobarPostulante(Long idPostulante) {
        Postulante p = postulanteRepository.findById(idPostulante).orElseThrow();
        p.setEstado(EstadoPostulante.APROBADO);
        
        // Crear Usuario (La contraseña por defecto es su DNI)
        Usuario u = Usuario.builder()
                .dni(p.getDni()).nombres(p.getNombres()).apellidos(p.getApellidos())
                .email(p.getCorreo()).passwordHash(passwordEncoder.encode(p.getDni()))
                .rol(UserRole.ALUMNO).build();
        u = usuarioRepository.save(u);

        // Crear Perfil Alumno
        Alumno a = Alumno.builder().usuario(u).carrera(p.getCarrera()).build();
        alumnoRepository.save(a);
        
        return "Aprobado. Credenciales generadas. Email: " + u.getEmail() + " | Clave: " + u.getDni();
    }
}
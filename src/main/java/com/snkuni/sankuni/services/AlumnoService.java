package com.snkuni.sankuni.services;

import com.snkuni.sankuni.dtos.AlumnoDTO;
import com.snkuni.sankuni.dtos.PostulanteDTO;
import com.snkuni.sankuni.dtos.UsuarioDTO;
import com.snkuni.sankuni.models.Alumno;
import com.snkuni.sankuni.models.Carrera;
import com.snkuni.sankuni.models.CuotaAlumno;
import com.snkuni.sankuni.models.Usuario;
import com.snkuni.sankuni.models.enums.EstadoCuota;
import com.snkuni.sankuni.models.enums.UserRole;
import com.snkuni.sankuni.repositories.AlumnoRepository;
import com.snkuni.sankuni.repositories.CarreraRepository;
import com.snkuni.sankuni.repositories.CuotaAlumnoRepository;
import com.snkuni.sankuni.repositories.UsuarioRepository;
import com.snkuni.sankuni.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlumnoService {

    private final AlumnoRepository alumnoRepository;
    // INYECCIONES FALTANTES AGREGADAS:
    private final UsuarioRepository usuarioRepository;
    private final CarreraRepository carreraRepository;
    private final CuotaAlumnoRepository cuotaRepository;
    private final PasswordEncoder passwordEncoder;

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

    @Transactional(readOnly = true)
    public AlumnoDTO buscarPorDni(String dni) {
        return alumnoRepository.findByUsuario_Dni(dni).map(this::mapearAAlumnoDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Alumno con DNI " + dni + " no encontrado"));
    }

    @Transactional
    public AlumnoDTO registroManual(PostulanteDTO dto) {
        // 1. Crear Usuario directamente
        Usuario u = Usuario.builder()
                .dni(dto.getDni()).nombres(dto.getNombres()).apellidos(dto.getApellidos())
                .email(dto.getCorreo()).passwordHash(passwordEncoder.encode(dto.getDni()))
                .rol(UserRole.ALUMNO).build();
        u = usuarioRepository.save(u);
        
        // 2. Crear Alumno
        Carrera carrera = carreraRepository.findById(dto.getCarreraId()).orElseThrow();
        Alumno a = Alumno.builder().usuario(u).carrera(carrera).build();
        a = alumnoRepository.save(a);
        
        // 3. Generar Cuota Inicial
        CuotaAlumno primeraCuota = CuotaAlumno.builder()
                .alumno(a).cicloAcademico("2026-I").mesCorrespondiente("Matrícula (Manual)")
                .montoTotal(new java.math.BigDecimal("150.00"))
                .estado(EstadoCuota.PENDIENTE).fechaVencimiento(java.time.LocalDate.now().plusDays(7))
                .build();
        cuotaRepository.save(primeraCuota);
        
        return mapearAAlumnoDTO(a);
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
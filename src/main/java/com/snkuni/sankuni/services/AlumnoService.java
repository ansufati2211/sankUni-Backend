package com.snkuni.sankuni.services;

import com.snkuni.sankuni.dtos.AlumnoDTO;
import com.snkuni.sankuni.dtos.PostulanteDTO;
import com.snkuni.sankuni.dtos.UsuarioDTO;
import com.snkuni.sankuni.models.Alumno;
import com.snkuni.sankuni.models.Carrera;
import com.snkuni.sankuni.models.CuotaAlumno;
import com.snkuni.sankuni.models.Matricula;
import com.snkuni.sankuni.models.Seccion;
import com.snkuni.sankuni.models.Usuario;
import com.snkuni.sankuni.models.enums.EstadoCuota;
import com.snkuni.sankuni.models.enums.UserRole;
import com.snkuni.sankuni.repositories.AlumnoRepository;
import com.snkuni.sankuni.repositories.CarreraRepository;
import com.snkuni.sankuni.repositories.CuotaAlumnoRepository;
import com.snkuni.sankuni.repositories.MatriculaRepository;
import com.snkuni.sankuni.repositories.SeccionRepository;
import com.snkuni.sankuni.repositories.UsuarioRepository;
import com.snkuni.sankuni.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlumnoService {

    private final AlumnoRepository alumnoRepository;
    private final UsuarioRepository usuarioRepository;
    private final CarreraRepository carreraRepository;
    private final CuotaAlumnoRepository cuotaRepository;
    private final PasswordEncoder passwordEncoder;
    private final SeccionRepository seccionRepository;
    private final MatriculaRepository matriculaRepository;

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
        
        // 3. Generar Paquete Financiero (Matrícula + 5 Pensiones para el Cachimbo)
        List<CuotaAlumno> nuevasCuotas = new ArrayList<>();

        // A. La Matrícula
        nuevasCuotas.add(CuotaAlumno.builder()
                .alumno(a).cicloAcademico("2026-I").mesCorrespondiente("Matrícula (Manual)")
                .montoTotal(new BigDecimal("150.00"))
                .estado(EstadoCuota.PENDIENTE)
                .fechaVencimiento(LocalDate.now().plusDays(7))
                .build());

        // B. Las 5 Pensiones automáticas
        BigDecimal montoPension = new BigDecimal("350.00");
        for (int i = 1; i <= 5; i++) {
            nuevasCuotas.add(CuotaAlumno.builder()
                    .alumno(a).cicloAcademico("2026-I")
                    .mesCorrespondiente("Pensión " + i + " - 2026-I")
                    .montoTotal(montoPension)
                    .estado(EstadoCuota.PENDIENTE)
                    .fechaVencimiento(LocalDate.now().plusDays(30L * i)) // Cada pensión vence 30 días después
                    .build());
        }

        // Guardamos todo de golpe en la base de datos
        cuotaRepository.saveAll(nuevasCuotas);
        
        // ==========================================
        // 4. AUTO-MATRÍCULA ACADÉMICA
        // ==========================================
        // Buscamos las clases de primer ciclo de su carrera
        List<Seccion> clasesPrimerCiclo = seccionRepository.findByCarreraAndCiclo(carrera.getIdCarrera(), "2026-I");
        List<Matricula> nuevasMatriculas = new ArrayList<>();
        
        for (Seccion sec : clasesPrimerCiclo) {
            nuevasMatriculas.add(Matricula.builder()
                    .alumno(a)
                    .seccion(sec)
                    .build());
        }
        
        // Inscribimos al alumno en todas sus clases de golpe
        matriculaRepository.saveAll(nuevasMatriculas);
        
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
package com.snkuni.sankuni.services;

import com.snkuni.sankuni.dtos.PostulanteDTO;
import com.snkuni.sankuni.exceptions.BusinessLogicException;
import com.snkuni.sankuni.exceptions.ResourceNotFoundException;
import com.snkuni.sankuni.models.*;
import com.snkuni.sankuni.models.enums.EstadoCuota;
import com.snkuni.sankuni.models.enums.EstadoPostulante;
import com.snkuni.sankuni.models.enums.UserRole;
import com.snkuni.sankuni.repositories.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostulanteService {

    private final PostulanteRepository postulanteRepository;
    private final CarreraRepository carreraRepository;
    private final UsuarioRepository usuarioRepository;
    private final AlumnoRepository alumnoRepository;
    private final CuotaAlumnoRepository cuotaRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService; // INYECCIÓN ACTUALIZADA
    private final SeccionRepository seccionRepository;
    private final MatriculaRepository matriculaRepository;

    public PostulanteService(PostulanteRepository postulanteRepository,
                             CarreraRepository carreraRepository,
                             UsuarioRepository usuarioRepository,
                             AlumnoRepository alumnoRepository,
                             CuotaAlumnoRepository cuotaRepository,
                             PasswordEncoder passwordEncoder,
                             EmailService emailService, // INYECCIÓN ACTUALIZADA
                             SeccionRepository seccionRepository,
                             MatriculaRepository matriculaRepository) {
        this.postulanteRepository = postulanteRepository;
        this.carreraRepository = carreraRepository;
        this.usuarioRepository = usuarioRepository;
        this.alumnoRepository = alumnoRepository;
        this.cuotaRepository = cuotaRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService; // INYECCIÓN ACTUALIZADA
        this.seccionRepository = seccionRepository;
        this.matriculaRepository = matriculaRepository;
    }

    @Transactional
    public PostulanteDTO registrar(PostulanteDTO dto) {
        Carrera carrera = carreraRepository.findById(dto.getCarreraId())
                .orElseThrow(() -> new ResourceNotFoundException("Carrera no encontrada"));

        Postulante postulante = Postulante.builder()
                .dni(dto.getDni())
                .nombres(dto.getNombres())
                .apellidos(dto.getApellidos())
                .correo(dto.getCorreo())
                .carrera(carrera)
                .sede(dto.getSede())
                .turno(dto.getTurno())
                .estado(EstadoPostulante.EN_REVISION)
                .build();

        Postulante guardado = postulanteRepository.save(postulante);
        return mapearADTO(guardado);
    }

    @Transactional(readOnly = true)
    public List<PostulanteDTO> listarPendientes() {
        return postulanteRepository.findByEstado(EstadoPostulante.EN_REVISION)
                .stream()
                .map(this::mapearADTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public String aprobarPostulante(Long idPostulante) {
        Postulante postulante = postulanteRepository.findById(idPostulante)
                .orElseThrow(() -> new ResourceNotFoundException("Postulante no encontrado"));

        if (postulante.getEstado() != EstadoPostulante.EN_REVISION) {
            throw new BusinessLogicException("El postulante ya ha sido procesado anteriormente.");
        }

        if (usuarioRepository.existsByEmail(postulante.getCorreo()) || usuarioRepository.existsByDni(postulante.getDni())) {
            throw new BusinessLogicException("Error: El DNI o Correo ya existen en el sistema.");
        }

        // 1. Actualizar estado
        postulante.setEstado(EstadoPostulante.APROBADO);
        postulanteRepository.save(postulante);

        // 2. Crear Usuario
        Usuario nuevoUsuario = Usuario.builder()
                .dni(postulante.getDni())
                .nombres(postulante.getNombres())
                .apellidos(postulante.getApellidos())
                .email(postulante.getCorreo())
                .passwordHash(passwordEncoder.encode(postulante.getDni()))
                .rol(UserRole.ALUMNO)
                .build();
        usuarioRepository.save(nuevoUsuario);

        // 3. Crear Alumno
        Alumno nuevoAlumno = Alumno.builder()
                .usuario(nuevoUsuario)
                .carrera(postulante.getCarrera())
                .build();
        alumnoRepository.save(nuevoAlumno);

        // 4. Generar Cuota de Ingreso
        List<CuotaAlumno> nuevasCuotas = new ArrayList<>();
        BigDecimal montoPension = new BigDecimal("150.00");
        nuevasCuotas.add(CuotaAlumno.builder()
                .alumno(nuevoAlumno)
                .cicloAcademico("2026-I")
                .mesCorrespondiente("Matrícula de Ingreso")
                .montoTotal(montoPension)
                .estado(EstadoCuota.PENDIENTE)
                .fechaVencimiento(LocalDate.now().plusDays(7))
                .build());
        cuotaRepository.saveAll(nuevasCuotas);

        // 5. Enviar correo asíncrono (NO BLOQUEA EL SISTEMA)
        emailService.enviarCorreoBienvenida(nuevoUsuario.getEmail(), nuevoUsuario.getEmail(), nuevoUsuario.getDni());

        return "Aprobado. Credenciales generadas y paquete financiero asignado.";
    }

    @Transactional
    public String rechazarPostulante(Long idPostulante) {
        Postulante postulante = postulanteRepository.findById(idPostulante)
                .orElseThrow(() -> new ResourceNotFoundException("Postulante no encontrado"));

        postulante.setEstado(EstadoPostulante.RECHAZADO);
        postulanteRepository.save(postulante);

        return "El postulante ha sido rechazado correctamente.";
    }

    private PostulanteDTO mapearADTO(Postulante postulante) {
        return PostulanteDTO.builder()
                .idPostulante(postulante.getIdPostulante())
                .dni(postulante.getDni())
                .nombres(postulante.getNombres())
                .apellidos(postulante.getApellidos())
                .correo(postulante.getCorreo())
                .carreraId(postulante.getCarrera() != null ? postulante.getCarrera().getIdCarrera() : null)
                .nombreCarrera(postulante.getCarrera() != null ? postulante.getCarrera().getNombre() : null)
                .sede(postulante.getSede())
                .turno(postulante.getTurno())
                .estado(postulante.getEstado().name())
                .build();
    }
}
// src/main/java/com/snkuni/sankuni/services/PostulanteService.java
package com.snkuni.sankuni.services;

import com.snkuni.sankuni.dtos.PostulanteDTO;
import com.snkuni.sankuni.models.*;
import com.snkuni.sankuni.models.enums.*;
import com.snkuni.sankuni.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostulanteService {
    private final PostulanteRepository postulanteRepository;
    private final CarreraRepository carreraRepository;
    private final UsuarioRepository usuarioRepository;
    private final AlumnoRepository alumnoRepository;
    private final CuotaAlumnoRepository cuotaRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;

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

        // 1. Crear Usuario
        Usuario u = Usuario.builder()
                .dni(p.getDni()).nombres(p.getNombres()).apellidos(p.getApellidos())
                .email(p.getCorreo()).passwordHash(passwordEncoder.encode(p.getDni()))
                .rol(UserRole.ALUMNO).build();
        u = usuarioRepository.save(u);

        // 2. Crear Perfil Alumno
        Alumno a = Alumno.builder().usuario(u).carrera(p.getCarrera()).build();
        a = alumnoRepository.save(a);

        // 3. Generar su primera Cuota de Matrícula
        CuotaAlumno primeraCuota = CuotaAlumno.builder()
                .alumno(a)
                .cicloAcademico("2026-I")
                .mesCorrespondiente("Matrícula de Ingreso")
                .montoTotal(new BigDecimal("150.00"))
                .estado(EstadoCuota.PENDIENTE)
                .fechaVencimiento(LocalDate.now().plusDays(7)) // Tiene 7 días para pagar
                .build();
        cuotaRepository.save(primeraCuota);

        // 4. Enviar Correo Electrónico Automático
        enviarCorreoBienvenida(u.getEmail(), u.getNombres(), u.getEmail(), p.getDni());

        return "Aprobado. Credenciales generadas y primera cuota asignada.";
    }

    private void enviarCorreoBienvenida(String correoDestino, String nombres, String usuarioLogin, String claveTemporal) {
        try {
            SimpleMailMessage mensaje = new SimpleMailMessage();
            mensaje.setTo(correoDestino);
            mensaje.setSubject("¡Bienvenido al Instituto Tech! - Credenciales de Acceso");
            mensaje.setText("Hola " + nombres + ",\n\n"
                    + "¡Felicidades! Tu matrícula ha sido aprobada exitosamente.\n\n"
                    + "Aquí tienes tus credenciales para acceder a la Intranet de estudiantes:\n"
                    + "Usuario: " + usuarioLogin + "\n"
                    + "Contraseña temporal: " + claveTemporal + "\n\n"
                    + "Por razones de seguridad, te recomendamos cambiar tu contraseña al ingresar por primera vez.\n\n"
                    + "Atentamente,\n"
                    + "Coordinación Académica - Instituto Tech");
            mailSender.send(mensaje);
        } catch (Exception e) {
            System.err.println("Error al enviar el correo de bienvenida: " + e.getMessage());
        }
    }
}
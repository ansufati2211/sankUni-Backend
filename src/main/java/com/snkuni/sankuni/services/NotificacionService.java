package com.snkuni.sankuni.services;

import com.snkuni.sankuni.dtos.NotificacionDTO;
import com.snkuni.sankuni.models.enums.EstadoPostulante;
import com.snkuni.sankuni.models.enums.RequestStatus;
import com.snkuni.sankuni.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificacionService {

    private final PostulanteRepository postulanteRepo;
    private final SolicitudRepository solicitudRepo;
    private final AlertaAcademicaRepository alertaRepo;
    private final MensajeContactoRepository mensajeRepo;
    private final MatriculaRepository matriculaRepo;

    public List<NotificacionDTO> obtenerNotificacionesAdmin() {
        List<NotificacionDTO> notificaciones = new ArrayList<>();

        // 1. Nuevos Postulantes
        notificaciones.addAll(postulanteRepo.findByEstado(EstadoPostulante.EN_REVISION).stream()
                .map(p -> NotificacionDTO.builder()
                        .idOrigen(p.getIdPostulante())
                        .tipo("POSTULANTE")
                        .titulo("Nuevo Aspirante Registrado")
                        .desc(p.getNombres() + " " + p.getApellidos() + " ha enviado su solicitud.")
                        .fecha(p.getFechaPostulacion())
                        .tiempo(calcularTiempo(p.getFechaPostulacion()))
                        .build())
                .collect(Collectors.toList()));

        // 2. Trámites SAE
        notificaciones.addAll(solicitudRepo.findByEstado(RequestStatus.PENDIENTE).stream()
                .map(s -> NotificacionDTO.builder()
                        .idOrigen(s.getIdSolicitud())
                        .tipo("SAE")
                        .titulo("Nuevo Trámite SAE")
                        .desc(s.getEmisor().getNombreCompleto() + " solicita: " + s.getTipo().name().replace("_", " "))
                        .fecha(s.getFechaSolicitud())
                        .tiempo(calcularTiempo(s.getFechaSolicitud()))
                        .build())
                .collect(Collectors.toList()));

        // 3. Alertas Académicas
        notificaciones.addAll(alertaRepo.findByResueltaFalse().stream()
                .map(a -> NotificacionDTO.builder()
                        .idOrigen(a.getIdAlerta())
                        .tipo("ALERTA")
                        .titulo("Alerta Académica")
                        .desc(a.getMensaje())
                        .fecha(a.getFechaCreacion())
                        .tiempo(calcularTiempo(a.getFechaCreacion()))
                        .build())
                .collect(Collectors.toList()));

        // 4. Mensajes Web
        notificaciones.addAll(mensajeRepo.findByEstadoAtencionFalse().stream()
                .map(m -> NotificacionDTO.builder()
                        .idOrigen(m.getIdMensaje())
                        .tipo("CONTACTO")
                        .titulo("Consulta Web")
                        .desc(m.getNombreCompleto() + " pregunta por: " + (m.getProgramaInteres() != null ? m.getProgramaInteres() : "Información general"))
                        .fecha(m.getFechaEnvio())
                        .tiempo(calcularTiempo(m.getFechaEnvio()))
                        .build())
                .collect(Collectors.toList()));

        // 5. Nuevas Matrículas (últimos 30 días)
        notificaciones.addAll(buildMatriculaNotifs(30));

        return notificaciones.stream()
                .sorted(Comparator.comparing(NotificacionDTO::getFecha).reversed())
                .limit(20)
                .collect(Collectors.toList());
    }

    public List<NotificacionDTO> obtenerNotificacionesCoordinador() {
        List<NotificacionDTO> notificaciones = new ArrayList<>();

        // Alertas académicas pendientes
        notificaciones.addAll(alertaRepo.findByResueltaFalse().stream()
                .map(a -> NotificacionDTO.builder()
                        .idOrigen(a.getIdAlerta())
                        .tipo("ALERTA")
                        .titulo("Alerta Académica")
                        .desc(a.getMensaje())
                        .fecha(a.getFechaCreacion())
                        .tiempo(calcularTiempo(a.getFechaCreacion()))
                        .build())
                .collect(Collectors.toList()));

        // Nuevas matrículas (últimos 30 días)
        notificaciones.addAll(buildMatriculaNotifs(30));

        return notificaciones.stream()
                .sorted(Comparator.comparing(NotificacionDTO::getFecha).reversed())
                .limit(20)
                .collect(Collectors.toList());
    }

    private List<NotificacionDTO> buildMatriculaNotifs(int dias) {
        return matriculaRepo.findMatriculasRecientes(LocalDateTime.now().minusDays(dias)).stream()
                .map(m -> NotificacionDTO.builder()
                        .idOrigen(m.getIdMatricula())
                        .tipo("MATRICULA")
                        .titulo("Nueva Matrícula")
                        .desc(m.getAlumno().getUsuario().getNombreCompleto()
                                + " se matriculó en: " + m.getSeccion().getCurso().getNombre()
                                + " (" + m.getSeccion().getCicloAcademico() + ")")
                        .fecha(m.getFechaMatricula())
                        .tiempo(calcularTiempo(m.getFechaMatricula()))
                        .build())
                .collect(Collectors.toList());
    }

    private String calcularTiempo(LocalDateTime fechaOrigen) {
        if (fechaOrigen == null) return "Reciente";
        Duration duracion = Duration.between(fechaOrigen, LocalDateTime.now());
        long minutos = duracion.toMinutes();
        if (minutos < 60) return "Hace " + (minutos == 0 ? "un momento" : minutos + " min");
        long horas = duracion.toHours();
        if (horas < 24) return "Hace " + horas + " horas";
        return "Hace " + duracion.toDays() + " días";
    }
}
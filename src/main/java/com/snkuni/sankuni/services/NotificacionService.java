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

    public List<NotificacionDTO> obtenerNotificacionesAdmin() {
        List<NotificacionDTO> notificaciones = new ArrayList<>();

        // 1. Nuevos Postulantes (Optimizada con findByEstado)
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

        // 2. Trámites SAE (Optimizada con findByEstado)
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

        // 3. Alertas Académicas (Optimizada con findByResueltaFalse)
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

        // 4. Mensajes Web (Optimizada con findByEstadoAtencionFalse)
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

        // ORDENAR TODAS LAS NOTIFICACIONES (De la más reciente a la más antigua) y limitar a 20
        return notificaciones.stream()
                .sorted(Comparator.comparing(NotificacionDTO::getFecha).reversed())
                .limit(20) // Limitamos a 20 para no saturar la UI visualmente
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
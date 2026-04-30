package com.snkuni.sankuni.services;

import com.snkuni.sankuni.dtos.SolicitudDTO;
import com.snkuni.sankuni.dtos.SolicitudRequestDTO;
import com.snkuni.sankuni.models.Seccion;
import com.snkuni.sankuni.models.Solicitud;
import com.snkuni.sankuni.models.Usuario;
import com.snkuni.sankuni.models.enums.RequestStatus;
import com.snkuni.sankuni.models.enums.TipoSolicitud;
import com.snkuni.sankuni.repositories.SeccionRepository;
import com.snkuni.sankuni.repositories.SolicitudRepository;
import com.snkuni.sankuni.repositories.UsuarioRepository;
import com.snkuni.sankuni.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SolicitudService {

    private final SolicitudRepository solicitudRepository;
    private final UsuarioRepository usuarioRepository;
    private final SeccionRepository seccionRepository;

    @Transactional
    public SolicitudDTO crearSolicitud(SolicitudRequestDTO request) {
        Usuario emisor = usuarioRepository.findById(request.getEmisorId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        Seccion seccion = null;
        if (request.getSeccionId() != null) {
            seccion = seccionRepository.findById(request.getSeccionId()).orElse(null);
        }
        Solicitud solicitud = Solicitud.builder()
                .emisor(emisor).tipo(TipoSolicitud.valueOf(request.getTipo()))
                .seccion(seccion).descripcion(request.getDescripcion())
                .estado(RequestStatus.PENDIENTE).build();
        return mapearADTO(solicitudRepository.save(solicitud));
    }

    @Transactional(readOnly = true)
    public List<SolicitudDTO> listarMisSolicitudes(Long idUsuario) {
        return solicitudRepository.findByEmisor_IdUsuario(idUsuario).stream().map(this::mapearADTO).toList();
    }

    // NUEVO: Para el panel del ADMIN (Todos los trámites)
    @Transactional(readOnly = true)
    public List<SolicitudDTO> listarTodas() {
        return solicitudRepository.findAll().stream().map(this::mapearADTO).toList();
    }

    // NUEVO: Para que el ADMIN acepte o rechace
    @Transactional
    public SolicitudDTO responderSolicitud(Long idSolicitud, String estado, String observacion) {
        Solicitud s = solicitudRepository.findById(idSolicitud)
                .orElseThrow(() -> new ResourceNotFoundException("Trámite no encontrado"));
        
        s.setEstado(RequestStatus.valueOf(estado.toUpperCase()));
        s.setObservacionCoordinador(observacion);
        s.setFechaRespuesta(LocalDateTime.now());
        
        return mapearADTO(solicitudRepository.save(s));
    }

    private SolicitudDTO mapearADTO(Solicitud s) {
        return SolicitudDTO.builder()
                .idSolicitud(s.getIdSolicitud())
                .nombreEmisor(s.getEmisor().getNombreCompleto())
                .tipo(s.getTipo().name())
                .cursoYSeccion(s.getSeccion() != null ? s.getSeccion().getCurso().getNombre() : "N/A")
                .descripcion(s.getDescripcion())
                .estado(s.getEstado().name())
                .fechaSolicitud(s.getFechaSolicitud())
                .fechaRespuesta(s.getFechaRespuesta())
                .observacionCoordinador(s.getObservacionCoordinador())
                .build();
    }
}
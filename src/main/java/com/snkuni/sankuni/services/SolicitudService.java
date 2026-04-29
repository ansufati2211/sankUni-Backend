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

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SolicitudService {

    private final SolicitudRepository solicitudRepository;
    private final UsuarioRepository usuarioRepository;
    private final SeccionRepository seccionRepository;

    @Transactional
    public SolicitudDTO crearSolicitud(SolicitudRequestDTO request) {
        // ... (el código de creación se mantiene exactamente igual) ...
        Usuario emisor = usuarioRepository.findById(request.getEmisorId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario emisor no encontrado"));

        Seccion seccion = null;
        if (request.getSeccionId() != null) {
            seccion = seccionRepository.findById(request.getSeccionId()).orElse(null);
        }

        Solicitud solicitud = Solicitud.builder()
                .emisor(emisor)
                .tipo(TipoSolicitud.valueOf(request.getTipo()))
                .seccion(seccion)
                .descripcion(request.getDescripcion())
                .estado(RequestStatus.PENDIENTE)
                .build();

        Solicitud guardada = solicitudRepository.save(solicitud);
        return mapearADTO(guardada);
    }

    @Transactional(readOnly = true)
    public List<SolicitudDTO> listarMisSolicitudes(UUID idUsuario) {
        return solicitudRepository.findByEmisor_IdUsuario(idUsuario).stream()
                .map(this::mapearADTO)
                .toList();
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
package com.snkuni.sankuni.dtos;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class SolicitudDTO {
    private UUID idSolicitud;
    private String nombreEmisor;
    private String tipo;
    private String cursoYSeccion;
    private String descripcion;
    private String estado;
    private LocalDateTime fechaSolicitud;
    private LocalDateTime fechaRespuesta;
    private String observacionCoordinador;
}
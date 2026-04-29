package com.snkuni.sankuni.dtos;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;


@Data
@Builder
public class SolicitudDTO {
    private Long idSolicitud;
    private String nombreEmisor;
    private String tipo;
    private String cursoYSeccion;
    private String descripcion;
    private String estado;
    private LocalDateTime fechaSolicitud;
    private LocalDateTime fechaRespuesta;
    private String observacionCoordinador;
}
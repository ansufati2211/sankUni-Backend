package com.snkuni.sankuni.dtos;

import lombok.*;
import java.math.BigDecimal; // <-- AGREGADO
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AlumnoDTO {
    private Long idAlumno;
    private UsuarioDTO usuario;
    private String nombreCarrera;
    private String estado;
    private LocalDate fechaIngreso;
    private BigDecimal promedioHistorico; // <-- ASEGÚRATE QUE SEA BigDecimal
}
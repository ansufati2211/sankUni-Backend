package com.snkuni.sankuni.dtos;

import lombok.*;
import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DashboardAdminDTO {
    private Integer totalAlumnos;
    private Integer totalDocentes;
    private BigDecimal ingresosMes; // <-- El nombre correcto para enlazar con JS
    private Integer solicitudesPendientes;
}
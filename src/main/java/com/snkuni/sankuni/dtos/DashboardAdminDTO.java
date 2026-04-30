package com.snkuni.sankuni.dtos;

import lombok.*;
import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DashboardAdminDTO {
    private Integer totalAlumnos;
    private BigDecimal ingresosTotales;
    private Integer cuotasVencidas;
}
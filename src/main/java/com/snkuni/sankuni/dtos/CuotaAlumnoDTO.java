package com.snkuni.sankuni.dtos;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CuotaAlumnoDTO {
    private Long idCuota;
    private Long idAlumno;
    private String cicloAcademico;
    private String mesCorrespondiente;
    private BigDecimal montoTotal;
    private String estado; // PENDIENTE, PAGADO, VENCIDO
    private LocalDate fechaVencimiento;
}
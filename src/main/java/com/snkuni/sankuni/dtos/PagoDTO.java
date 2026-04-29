package com.snkuni.sankuni.dtos;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
@Builder
public class PagoDTO {
    private Long idPago;
    private Long idMatricula;
    private BigDecimal monto;
    private String concepto;
    private LocalDateTime fechaPago;
}
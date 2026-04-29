package com.snkuni.sankuni.dtos;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class PagoDTO {
    private UUID idPago;
    private UUID idMatricula;
    private BigDecimal monto;
    private String concepto;
    private LocalDateTime fechaPago;
}